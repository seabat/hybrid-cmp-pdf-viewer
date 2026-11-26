package dev.seabat.cmp.pdfviewer.viewcontroller.top

/**
 * iOS の PDF 取り込み（ドキュメントピッカー・他アプリからの「開く」「共有」）から
 * Kotlin の ViewModel へ PDF 追加を橋渡しするクラス
 *
 * Swift 側から pdfAddBridge.add(sourceUrl:name:createdAt:size:openAfterAdd:) を呼ぶだけでよい。
 * onAdd は TopContentViewController の DisposableEffect 内で設定される。
 *
 * 他アプリから PDF を渡されてアプリが起動する場合、Swift の onOpenURL は Compose の合成より
 * 先に発火して onAdd が未設定になりうる。取りこぼさないよう未処理分を保留し、
 * onAdd がセットされた時点でまとめて流す
 */
class PdfAddBridge {
    private data class PendingPdf(
        val sourceUrl: String,
        val name: String,
        val createdAt: String,
        val size: String,
        val openAfterAdd: Boolean
    )

    private val pendingList = mutableListOf<PendingPdf>()

    internal var onAdd: ((String, String, String, String, Boolean) -> Unit)? = null
        set(value) {
            field = value
            if (value == null) return
            // 保留中のものを流す。流している途中で再追加されないよう先にリストを空にする
            val flushed = pendingList.toList()
            pendingList.clear()
            flushed.forEach { value(it.sourceUrl, it.name, it.createdAt, it.size, it.openAfterAdd) }
        }

    /**
     * PDF を一覧へ追加する
     *
     * @param openAfterAdd 追加後に Viewer を開く場合は true（他アプリからの「開く」「共有」で使う）
     */
    fun add(
        sourceUrl: String,
        name: String,
        createdAt: String,
        size: String,
        openAfterAdd: Boolean = false
    ) {
        val onAdd = this.onAdd
        if (onAdd == null) {
            pendingList.add(PendingPdf(sourceUrl, name, createdAt, size, openAfterAdd))
        } else {
            onAdd(sourceUrl, name, createdAt, size, openAfterAdd)
        }
    }
}
