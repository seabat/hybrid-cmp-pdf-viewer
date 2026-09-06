package dev.seabat.cmp.pdfviewer.viewcontroller.top

/**
 * iOS の TopHeader（別 ComposeUIViewController）から TopContent 側の ViewModel へ
 * 並び替えボトムシートの表示要求を橋渡しするクラス
 *
 * Swift 側から sortSheetBridge.open() を呼ぶだけでよい。
 * onOpen は TopContentViewController の DisposableEffect 内で設定される。
 */
class SortSheetBridge {
    internal var onOpen: (() -> Unit)? = null

    fun open() {
        onOpen?.invoke()
    }
}
