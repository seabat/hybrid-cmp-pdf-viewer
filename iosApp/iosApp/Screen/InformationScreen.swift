import SwiftUI
import ComposeApp
import LocalAuthentication

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
    @State private var showScreenLockAlert = false

    var body: some View {
        InformationScaffoldComposeView(
            onNavigateBack: { dismiss() },
            onShowAlert: { showAlert = true },
            onShowBioAuth: { showBioAuth = true }
        )
        .ignoresSafeArea()
        .navigationBarHidden(true)
        .onChange(of: showBioAuth) { _, newValue in
            if newValue {
                showBioAuth = false
                startBiometricAuth()
            }
        }
        .alert(StringResourcesKt.getString(key: .informationAlertTitle), isPresented: $showAlert) {
            Button(StringResourcesKt.getString(key: .alertOk)) { showAlert = false }
        } message: {
            Text(StringResourcesKt.getString(key: .informationAlertMessage))
        }
        .alert(StringResourcesKt.getString(key: .informationScreenLockTitle), isPresented: $showScreenLockAlert) {
            Button(StringResourcesKt.getString(key: .alertOk)) { showScreenLockAlert = false }
        } message: {
            Text(StringResourcesKt.getString(key: .informationScreenLockMessage))
        }
    }

    private func startBiometricAuth() {
        let context = LAContext()
        var error: NSError?
        guard context.canEvaluatePolicy(.deviceOwnerAuthentication, error: &error) else {
            showScreenLockAlert = true
            return
        }
        context.evaluatePolicy(
            .deviceOwnerAuthentication,
            localizedReason: StringResourcesKt.getString(key: .bioAuthSubtitle)
        ) { _, _ in }
    }
}
