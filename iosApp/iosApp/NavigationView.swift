import UIKit
import SwiftUI

// MARK: - 画面遷移先の管理

/// NavigationStack で管理する画面遷移先
enum Destination: Hashable {
    case viewer(fileName: String)
}

// MARK: - ナビゲーションビュー

/// アプリのナビゲーションを管理するビュー
/// NavigationStack 内で TopScreen / ViewerScreen 間の画面遷移を管理する
struct ContentView: View {
    @State private var path = [Destination]()

    var body: some View {
        NavigationStack(path: $path) {
            TopScreen(path: $path)
                .navigationDestination(for: Destination.self) { destination in
                    switch destination {
                    case .viewer(let fileName):
                        ViewerScreen(fileName: fileName)
                    }
                }
        }
    }
}

