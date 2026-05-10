import SwiftUI
import ComposeApp

/// Kotlin Composable の InformationScaffold を UIViewControllerRepresentable でラップするビュー
private struct InformationScaffoldComposeView: UIViewControllerRepresentable {
    var onNavigateBack: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        InformationScaffoldViewControllerKt.InformationScaffoldViewController {
            onNavigateBack()
        }
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// インフォメーションページ
/// View プロトコルを実装した SwiftUI コンポーネント
struct InformationScreen: View {
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        InformationScaffoldComposeView(onNavigateBack: { dismiss() })
            .ignoresSafeArea()
            .navigationBarHidden(true)
    }
}
