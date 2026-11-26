package dev.seabat.cmp.pdfviewer.screen.top

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.CopyPdfFileUseCaseContract
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.DeletePdfFileUseCaseContract
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.RenderPdfThumbnailUseCaseContract
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.ReadPdfListUseCaseContract
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.SavePdfListUseCaseContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.decodeToImageBitmap

/** PDF 一覧の並び替え対象の項目 */
enum class SortField {
    DATE,
    NAME
}

/** PDF 一覧の並び替えの向き */
enum class SortOrder {
    ASC,
    DESC
}

class TopViewModel(
    private val readPdfListUseCase: ReadPdfListUseCaseContract,
    private val savePdfListUseCase: SavePdfListUseCaseContract,
    private val copyPdfFileUseCase: CopyPdfFileUseCaseContract,
    private val deletePdfFileUseCase: DeletePdfFileUseCaseContract,
    private val renderPdfThumbnailUseCase: RenderPdfThumbnailUseCaseContract
) : ViewModel() {

    private val _rawPdfList = MutableStateFlow<List<PdfFile>>(emptyList())

    private val _sortField = MutableStateFlow(SortField.DATE)
    val sortField: StateFlow<SortField> = _sortField.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.DESC)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _isSortSheetVisible = MutableStateFlow(false)
    val isSortSheetVisible: StateFlow<Boolean> = _isSortSheetVisible.asStateFlow()

    private val _pdfList = MutableStateFlow<List<PdfFile>>(emptyList())
    val pdfList: StateFlow<List<PdfFile>> = _pdfList.asStateFlow()

    /** fileName をキーとしたサムネイルのキャッシュ。一覧のスクロールで再生成されるのを防ぐ */
    private val _thumbnails = MutableStateFlow<Map<String, ImageBitmap>>(emptyMap())
    val thumbnails: StateFlow<Map<String, ImageBitmap>> = _thumbnails.asStateFlow()

    /**
     * 他アプリから受け取った PDF の追加完了後に開くべきファイル名
     * Viewer へ遷移したら [consumePendingViewerFileName] でクリアする
     */
    private val _pendingViewerFileName = MutableStateFlow<String?>(null)
    val pendingViewerFileName: StateFlow<String?> = _pendingViewerFileName.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _rawPdfList.value = readPdfListUseCase()
                applySort()
                // 画面に見えているものから埋まるよう、ソート済みの表示順で生成する
                cacheMissingThumbnails(_pdfList.value)
            } catch (_: Exception) {
            }
        }
    }

    /**
     * PDF を内部ストレージへコピーして一覧に追加する
     *
     * @param openAfterAdd 追加後に Viewer を開く場合は true（他アプリからの「開く」「共有」で使う）。
     *   Viewer は永続化済みの一覧からファイルを検索するため、保存が完了してから
     *   [pendingViewerFileName] へ流す
     */
    fun addPdfFile(
        sourceUri: String,
        name: String,
        createdAt: String,
        size: String,
        openAfterAdd: Boolean = false
    ) {
        viewModelScope.launch {
            try {
                val uniqueName = toUniqueFileName(name)
                val filePath = copyPdfFileUseCase(sourceUri = sourceUri, destFileName = uniqueName)
                val newList = _rawPdfList.value + PdfFile(
                    fileName = uniqueName,
                    createdAt = createdAt,
                    size = size,
                    filePath = filePath
                )
                _rawPdfList.value = newList
                applySort()
                savePdfListUseCase(newList)
                if (openAfterAdd) {
                    _pendingViewerFileName.value = uniqueName
                }
                cacheMissingThumbnails(_pdfList.value)
            } catch (_: Exception) {
            }
        }
    }

    /** Viewer への遷移が済んだことを通知する */
    fun consumePendingViewerFileName() {
        _pendingViewerFileName.value = null
    }

    /**
     * 一覧内で重複しないファイル名を返す（重複する場合は "name (1).pdf" のように連番を付ける）
     *
     * fileName は一覧の key・サムネイルのキャッシュキー・Viewer の検索キーを兼ねているため、
     * 同じ PDF を繰り返し追加しても一意になるようにする
     */
    private fun toUniqueFileName(name: String): String {
        val existingNames = _rawPdfList.value.map { it.fileName }.toSet()
        if (name !in existingNames) return name

        val baseName = name.substringBeforeLast('.', name)
        val extension = name.substringAfterLast('.', "")
        val suffix = if (extension.isEmpty()) "" else ".$extension"
        var index = 1
        while ("$baseName ($index)$suffix" in existingNames) index++
        return "$baseName ($index)$suffix"
    }

    fun deletePdfFile(file: PdfFile) {
        viewModelScope.launch {
            try {
                deletePdfFileUseCase(file.filePath)
                val newList = _rawPdfList.value - file
                _rawPdfList.value = newList
                applySort()
                _thumbnails.update { cache -> cache - file.fileName }
                savePdfListUseCase(newList)
            } catch (_: Exception) {
            }
        }
    }

    fun showSortSheet() {
        _isSortSheetVisible.value = true
    }

    fun dismissSortSheet() {
        _isSortSheetVisible.value = false
    }

    fun selectSortOption(field: SortField, order: SortOrder) {
        _sortField.value = field
        _sortOrder.value = order
        applySort()
        _isSortSheetVisible.value = false
    }

    /**
     * 未取得のサムネイルを順次生成してキャッシュに追加する。
     * 1 件ずつ State を更新するため、生成済みのものから表示される
     */
    private suspend fun cacheMissingThumbnails(pdfList: List<PdfFile>) {
        pdfList.forEach { file ->
            if (file.filePath.isEmpty() || _thumbnails.value.containsKey(file.fileName)) return@forEach
            val bitmap = runCatching {
                renderPdfThumbnailUseCase(file.filePath, THUMBNAIL_WIDTH_PX)?.let { png ->
                    // デコードもメインスレッドを塞がないようワーカースレッドで行う
                    withContext(Dispatchers.Default) { png.decodeToImageBitmap() }
                }
            }.getOrNull() ?: return@forEach
            // 複数のコルーチンから同時に呼ばれても更新が失われないよう update を使う
            _thumbnails.update { cache -> cache + (file.fileName to bitmap) }
        }
    }

    private fun applySort() {
        val comparator = when (_sortField.value) {
            SortField.DATE -> compareBy<PdfFile> { it.createdAt }
            SortField.NAME -> compareBy { it.displayName.ifEmpty { it.fileName } }
        }
        val sorted = _rawPdfList.value.sortedWith(comparator)
        _pdfList.value = if (_sortOrder.value == SortOrder.DESC) sorted.asReversed() else sorted
    }

    companion object {
        /** サムネイルの生成幅 (px)。表示サイズ 48dp の約 3 倍を確保して高密度画面でも粗くならないようにする */
        private const val THUMBNAIL_WIDTH_PX = 144
    }
}
