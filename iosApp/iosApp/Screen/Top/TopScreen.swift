import UIKit
import SwiftUI
import SharedUI

/// Kotlin Composable の TopHeader を UIViewControllerRepresentable でラップするビュー
private struct TopHeaderComposeView: UIViewControllerRepresentable {
    var onNavigateToInformation: () -> Void
    var onAddPdf: () -> Void
    var onSortTapped: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        TopHeaderViewControllerKt.TopHeaderViewController(
            onNavigateToInformation: { onNavigateToInformation() },
            onAddPdf: { onAddPdf() },
            onSortTapped: { onSortTapped() }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// Kotlin Composable の TopContent を UIViewControllerRepresentable でラップするビュー
private struct TopContentComposeView: UIViewControllerRepresentable {
    var onNavigateToViewer: (String) -> Void
    var pdfAddBridge: PdfAddBridge
    var sortSheetBridge: SortSheetBridge

    func makeUIViewController(context: Context) -> UIViewController {
        TopContentViewControllerKt.TopContentViewController(
            onNavigateToViewer: { fileName in self.onNavigateToViewer(fileName) },
            pdfAddBridge: pdfAddBridge,
            sortSheetBridge: sortSheetBridge
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// PDF ビューアのトップページ
/// View プロトコルを実装した SwiftUI コンポーネント
struct TopScreen: View {
    @Binding var path: [Destination]
    @State private var showDocumentPicker = false
    @State private var pdfAddBridge = PdfAddBridge()
    @State private var sortSheetBridge = SortSheetBridge()

    var body: some View {
        VStack(spacing: 0) {
            TopHeaderComposeView(
                onNavigateToInformation: { path.append(.information) },
                onAddPdf: { showDocumentPicker = true },
                onSortTapped: { sortSheetBridge.open() }
            )
                .frame(height: 64)
                .background(
                    // TopHeader の TopAppBar と同じ色をステータスバー領域と
                    // 横向き時の左右のセーフエリアへ拡張する
                    Color(AppColors.shared.headerContainer).ignoresSafeArea(edges: [.top, .horizontal])
                )
            TopContentComposeView(
                onNavigateToViewer: { fileName in path.append(.viewer(fileName: fileName)) },
                pdfAddBridge: pdfAddBridge,
                sortSheetBridge: sortSheetBridge
            )
            .background(
                // TopContent の背景色（contentContainer）をナビゲーションバー領域と
                // 横向き時の左右のセーフエリアへ拡張する
                Color(AppColors.shared.contentContainer).ignoresSafeArea(edges: [.bottom, .horizontal])
            )
        }
        .sheet(isPresented: $showDocumentPicker) {
            DocumentPickerView { sourceUrl, name, createdAt, size in
                pdfAddBridge.add(sourceUrl: sourceUrl, name: name, createdAt: createdAt, size: size)
            }
        }
    }
}
