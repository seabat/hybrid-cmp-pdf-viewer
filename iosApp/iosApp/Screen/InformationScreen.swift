import SwiftUI
import ComposeApp

/// Kotlin Composable の InformationScaffold を UIViewControllerRepresentable でラップするビュー
private struct InformationScaffoldComposeView: UIViewControllerRepresentable {
    var onNavigateBack: () -> Void
    var onShowAlert: () -> Void
    var onShowBioAuth: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        InformationScaffoldViewControllerKt.InformationScaffoldViewController(
            onNavigateBack: { onNavigateBack() },
            onShowAlert: { onShowAlert() },
            onShowBioAuth: { onShowBioAuth() }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// インフォメーションページ
/// View プロトコルを実装した SwiftUI コンポーネント
struct InformationScreen: View {
    @Environment(\.dismiss) private var dismiss
    @State private var showAlert = false
    @State private var showBioAuth = false

    var body: some View {
        InformationScaffoldComposeView(
            onNavigateBack: { dismiss() },
            onShowAlert: {
                showAlert = true
            }
        )
        .ignoresSafeArea()
        .navigationBarHidden(true)
        .alert(StringResourcesKt.getString(key: .informationAlertTitle), isPresented: $showAlert) {
            Button(StringResourcesKt.getString(key: .alertOk)) { showAlert = false }
        } message: {
            Text(StringResourcesKt.getString(key: .informationAlertMessage))
        }
    }
}
