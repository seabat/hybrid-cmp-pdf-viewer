import UIKit
import SwiftUI
import SharedUI

/// Kotlin Composable の TopHeader を UIViewControllerRepresentable でラップするビュー
private struct TopHeaderComposeView: UIViewControllerRepresentable {
    var onNavigateToInformation: () -> Void
    var onAddPdf: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        TopHeaderViewControllerKt.TopHeaderViewController(
            onNavigateToInformation: { onNavigateToInformation() },
            onAddPdf: { onAddPdf() }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// Kotlin Composable の TopContent を UIViewControllerRepresentable でラップするビュー
private struct TopContentComposeView: UIViewControllerRepresentable {
    var onNavigateToViewer: (String) -> Void
    var pdfAddBridge: PdfAddBridge

    func makeUIViewController(context: Context) -> UIViewController {
        TopContentViewControllerKt.TopContentViewController(
            onNavigateToViewer: { fileName in self.onNavigateToViewer(fileName) },
            pdfAddBridge: pdfAddBridge
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

    var body: some View {
        VStack(spacing: 0) {
            TopHeaderComposeView(
                onNavigateToInformation: { path.append(.information) },
                onAddPdf: { showDocumentPicker = true }
            )
                .frame(height: 64)
                .background(
                    // TopHeader の TopAppBar と同じ色をステータスバー領域へ拡張
                    Color(AppColors.shared.headerContainer).ignoresSafeArea(edges: .top)
                )
            TopContentComposeView(
                onNavigateToViewer: { fileName in path.append(.viewer(fileName: fileName)) },
                pdfAddBridge: pdfAddBridge
            )
            .background(
                // TopContent の背景色（contentContainer）をナビゲーションバー領域へ拡張
                Color(AppColors.shared.contentContainer).ignoresSafeArea(edges: .bottom)
            )
        }
        .sheet(isPresented: $showDocumentPicker) {
            DocumentPickerView { sourceUrl, name, createdAt, size in
                pdfAddBridge.add(sourceUrl: sourceUrl, name: name, createdAt: createdAt, size: size)
            }
        }
    }
}
