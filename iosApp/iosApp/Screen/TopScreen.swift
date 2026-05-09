import UIKit
import SwiftUI
import ComposeApp

/// Kotlin Composable の TopHeader を UIViewControllerRepresentable でラップするビュー
private struct TopHeaderComposeView: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        TopHeaderViewControllerKt.TopHeaderViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// Kotlin Composable の TopScreen を UIViewControllerRepresentable でラップするビュー
private struct TopContentComposeView: UIViewControllerRepresentable {
    var onNavigateToViewer: (String) -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        TopContentViewControllerKt.TopContentViewController { fileName in
            self.onNavigateToViewer(fileName)
        }
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// PDF ビューアのトップページ
/// View プロトコルを実装した SwiftUI コンポーネント
struct TopScreen: View {
    @Binding var path: [Destination]

    var body: some View {
        VStack(spacing: 0) {
            TopHeaderComposeView()
                .frame(height: 64)
                .background(
                    // TopHeader の TopAppBar と同じ色をステータスバー領域へ拡張
                    Color(AppColors.shared.headerContainer).ignoresSafeArea(edges: .top)
                )
            TopContentComposeView(onNavigateToViewer: { fileName in
                path.append(.viewer(fileName: fileName))
            })
            .background(
                // TopContent の背景色（contentContainer）をナビゲーションバー領域へ拡張
                Color(AppColors.shared.contentContainer).ignoresSafeArea(edges: .bottom)
            )
        }
    }
}
