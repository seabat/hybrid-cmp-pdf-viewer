import SwiftUI
import ComposeApp

/// Kotlin Composable の ViewerScaffold を UIViewControllerRepresentable でラップするビュー
private struct ViewerScaffoldComposeView: UIViewControllerRepresentable {
    var fileName: String
    var onNavigateBack: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        ViewerScaffoldViewControllerKt.ViewerScaffoldViewController(fileName: fileName) {
            onNavigateBack()
        }
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// PDF ビューアページ
/// View プロトコルを実装した SwiftUI コンポーネント
struct ViewerScreen: View {
    var fileName: String
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        ViewerScaffoldComposeView(fileName: fileName, onNavigateBack: { dismiss() })
            .ignoresSafeArea()
            .navigationBarHidden(true)
    }
}
