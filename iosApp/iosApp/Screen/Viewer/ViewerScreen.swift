import SwiftUI
import ComposeApp

/// Kotlin Composable の ViewerScaffold を UIViewControllerRepresentable でラップするビュー
struct ViewerScaffoldComposeView: UIViewControllerRepresentable {
    var fileName: String
    var onNavigateBack: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        ViewerScaffoldViewControllerKt.ViewerScaffoldViewController(fileName: fileName) {
            onNavigateBack()
        }
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
