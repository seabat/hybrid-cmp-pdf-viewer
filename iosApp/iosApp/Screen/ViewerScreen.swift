import UIKit
import SwiftUI
import ComposeApp

/// Kotlin Composable の ViewerContent を UIViewControllerRepresentable でラップするビュー
private struct ViewerContentComposeView: UIViewControllerRepresentable {
    var fileName: String

    func makeUIViewController(context: Context) -> UIViewController {
        ViewerContentViewControllerKt.ViewerContentViewController(fileName: fileName)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// PDF ビューアページ
/// View プロトコルを実装した SwiftUI コンポーネント
struct ViewerScreen: View {
    var fileName: String

    var body: some View {
        ViewerContentComposeView(fileName: fileName)
            .ignoresSafeArea()
            .navigationTitle(fileName)
            .navigationBarTitleDisplayMode(.inline)
    }
}
