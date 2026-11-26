import UIKit
import SwiftUI

// MARK: - 画面遷移先の管理

/// NavigationStack で管理する画面遷移先
enum Destination: Hashable {
    case viewer(fileName: String)
    case information
}

// MARK: - ナビゲーションビュー

/// アプリのナビゲーションを管理するビュー
/// NavigationStack 内で TopScreen / ViewerScreen 間の画面遷移を管理する
struct NavigationView: View {
    /// 他アプリから渡された PDF の URL（iOSApp の onOpenURL から受け取る）
    @Binding var openedPdfUrl: URL?

    @State private var path = [Destination]()

    var body: some View {
        NavigationStack(path: $path) {
            TopScreen(path: $path, openedPdfUrl: $openedPdfUrl)
                .navigationDestination(for: Destination.self) { destination in
                    switch destination {
                    case .viewer(let fileName):
                        ViewerScaffoldComposeView(fileName: fileName, onNavigateBack: { path.removeLast() })
                            // ViewerScaffoldComposeView は updateUIViewController で中身を差し替えられないため、
                            // fileName が変わったら ViewController ごと作り直す
                            .id(fileName)
                            .ignoresSafeArea()
                            .navigationBarHidden(true)
                    case .information:
                        InformationScreen()
                    }
                }
        }
    }
}

