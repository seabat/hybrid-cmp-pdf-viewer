package dev.seabat.cmp.pdfviewer.viewcontroller.top

/**
 * iOS の UIDocumentPickerViewController から Kotlin の ViewModel へ PDF 追加を橋渡しするクラス
 *
 * Swift 側から pdfAddBridge.add(name:createdAt:size:) を呼ぶだけでよい。
 * onAdd は TopContentViewController の DisposableEffect 内で設定される。
 */
class PdfAddBridge {
    internal var onAdd: ((String, String, String, String) -> Unit)? = null

    fun add(sourceUrl: String, name: String, createdAt: String, size: String) {
        onAdd?.invoke(sourceUrl, name, createdAt, size)
    }
}
