import SwiftUI
import ComposeApp

/// Kotlin Composable の InformationScaffold を UIViewControllerRepresentable でラップするビュー
private struct InformationScaffoldComposeView: UIViewControllerRepresentable {
    var onNavigateBack: () -> Void
    var onShowAlert: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        InformationScaffoldViewControllerKt.InformationScaffoldViewController(
            onNavigateBack: { onNavigateBack() },
            onShowAlert: { onShowAlert() }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// インフォメーションページ
/// View プロトコルを実装した SwiftUI コンポーネント
struct InformationScreen: View {
    @Environment(\.dismiss) private var dismiss
    @State private var showAlert = false

    var body: some View {
        InformationScaffoldComposeView(
            onNavigateBack: { dismiss() },
            onShowAlert: { showAlert = true }
        )
        .ignoresSafeArea()
        .navigationBarHidden(true)
        .alert("確認", isPresented: $showAlert) {
            Button("OK") { showAlert = false }
        } message: {
            Text("このアプリは最新です。")
        }
    }
}
