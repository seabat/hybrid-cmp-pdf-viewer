import SwiftUI
import SharedUI

/// PDF 一覧の並び替え設定を表示するネイティブ SwiftUI シート
/// 項目（日付/ファイル名）・順番（昇順/降順）を選択すると即座に [SortSheetBridge] 経由で
/// Kotlin の TopViewModel へ反映し、シートを閉じる
struct SortOptionsView: View {
    var sortSheetBridge: SortSheetBridge
    @Environment(\.dismiss) private var dismiss

    // SharedUI.SortOrder は Foundation.SortOrder と型名が衝突するため、
    // モジュール名で明示的に修飾する
    @State private var selectedField: SortField
    @State private var selectedOrder: SharedUI.SortOrder

    init(sortSheetBridge: SortSheetBridge) {
        self.sortSheetBridge = sortSheetBridge
        _selectedField = State(initialValue: sortSheetBridge.currentSortField)
        _selectedOrder = State(initialValue: sortSheetBridge.currentSortOrder)
    }

    var body: some View {
        NavigationStack {
            List {
                Section("項目") {
                    optionRow(label: "日付", isSelected: selectedField == .date) {
                        select(field: .date, order: selectedOrder)
                    }
                    optionRow(label: "ファイル名", isSelected: selectedField == .name) {
                        select(field: .name, order: selectedOrder)
                    }
                }
                Section("順番") {
                    optionRow(label: "昇順", isSelected: selectedOrder == .asc) {
                        select(field: selectedField, order: .asc)
                    }
                    optionRow(label: "降順", isSelected: selectedOrder == .desc) {
                        select(field: selectedField, order: .desc)
                    }
                }
            }
            .navigationTitle("並び替え")
            .navigationBarTitleDisplayMode(.inline)
        }
    }

    private func select(field: SortField, order: SharedUI.SortOrder) {
        selectedField = field
        selectedOrder = order
        sortSheetBridge.selectSortOption(field: field, order: order)
        dismiss()
    }

    @ViewBuilder
    private func optionRow(label: String, isSelected: Bool, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            HStack {
                Text(label)
                    .foregroundColor(.primary)
                Spacer()
                if isSelected {
                    Image(systemName: "checkmark")
                        .foregroundColor(.accentColor)
                }
            }
        }
    }
}
