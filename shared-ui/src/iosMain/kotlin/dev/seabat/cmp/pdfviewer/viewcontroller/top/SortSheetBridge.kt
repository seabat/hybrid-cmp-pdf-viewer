package dev.seabat.cmp.pdfviewer.viewcontroller.top

import dev.seabat.cmp.pdfviewer.screen.top.SortField
import dev.seabat.cmp.pdfviewer.screen.top.SortOrder

/**
 * iOS の SwiftUI ネイティブなソートシートと Kotlin の TopViewModel を橋渡しするクラス
 *
 * TopContentViewController が現在のソート条件を [currentSortField] / [currentSortOrder] へ
 * 反映し続け、Swift 側はシート表示時にその値を読んでラジオボタンの初期選択に使う。
 * Swift 側で選択が行われたら selectSortOption(field:order:) を呼ぶだけでよい。
 */
class SortSheetBridge {
    var currentSortField: SortField = SortField.DATE
        internal set

    var currentSortOrder: SortOrder = SortOrder.DESC
        internal set

    internal var onSortOptionSelected: ((SortField, SortOrder) -> Unit)? = null

    fun selectSortOption(field: SortField, order: SortOrder) {
        onSortOptionSelected?.invoke(field, order)
    }
}
