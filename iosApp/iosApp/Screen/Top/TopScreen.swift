import UIKit
import SwiftUI
import SharedUI

/// Kotlin Composable の TopHeader を UIViewControllerRepresentable でラップするビュー
private struct TopHeaderComposeView: UIViewControllerRepresentable {
    var onNavigateToInformation: () -> Void
    var onAddPdf: () -> Void
    var onSortTapped: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        TopHeaderViewControllerKt.TopHeaderViewController(
            onNavigateToInformation: { onNavigateToInformation() },
            onAddPdf: { onAddPdf() },
            onSortTapped: { onSortTapped() }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// Kotlin Composable の TopContent を UIViewControllerRepresentable でラップするビュー
private struct TopContentComposeView: UIViewControllerRepresentable {
    var onNavigateToViewer: (String) -> Void
    var pdfAddBridge: PdfAddBridge
    var sortSheetBridge: SortSheetBridge

    func makeUIViewController(context: Context) -> UIViewController {
        TopContentViewControllerKt.TopContentViewController(
            onNavigateToViewer: { fileName in self.onNavigateToViewer(fileName) },
            pdfAddBridge: pdfAddBridge,
            sortSheetBridge: sortSheetBridge
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// PDF ビューアのトップページ
/// View プロトコルを実装した SwiftUI コンポーネント
struct TopScreen: View {
    @Binding var path: [Destination]
    /// 他アプリから渡された PDF の URL。取り込みが済んだら nil に戻す
    @Binding var openedPdfUrl: URL?

    /// 共有で復帰する途中（inactive）に画面遷移すると NavigationStack の状態がずれるため、
    /// アクティブになるのを待って取り込む
    @Environment(\.scenePhase) private var scenePhase

    @State private var showDocumentPicker = false
    @State private var pdfAddBridge = PdfAddBridge()
    @State private var sortSheetBridge = SortSheetBridge()

    var body: some View {
        VStack(spacing: 0) {
            TopHeaderComposeView(
                onNavigateToInformation: { path.append(.information) },
                onAddPdf: { showDocumentPicker = true },
                onSortTapped: { sortSheetBridge.open() }
            )
                .frame(height: 64)
                .background(
                    // TopHeader の TopAppBar と同じ色をステータスバー領域と
                    // 横向き時の左右のセーフエリアへ拡張する
                    Color(AppColors.shared.headerContainer).ignoresSafeArea(edges: [.top, .horizontal])
                )
            TopContentComposeView(
                onNavigateToViewer: { fileName in path.append(.viewer(fileName: fileName)) },
                pdfAddBridge: pdfAddBridge,
                sortSheetBridge: sortSheetBridge
            )
            .background(
                // TopContent の背景色（contentContainer）をナビゲーションバー領域と
                // 横向き時の左右のセーフエリアへ拡張する
                Color(AppColors.shared.contentContainer).ignoresSafeArea(edges: [.bottom, .horizontal])
            )
        }
        .sheet(isPresented: $showDocumentPicker) {
            DocumentPickerView { file in
                addPdf(file, openAfterAdd: false)
            }
        }
        // onChange ではなく task(id:) を使う。共有シートからの起動では
        // TopScreen の表示より先に onOpenURL が発火するため、初回表示時にも処理する必要がある
        .task(id: openedPdfUrl) {
            importOpenedPdfIfActive()
        }
        // onOpenURL はアプリがアクティブになる前に発火するため、
        // アクティブになった時点でも取り込みを試みる
        .onChange(of: scenePhase) { _, _ in
            importOpenedPdfIfActive()
        }
    }

    /// 他アプリから渡された PDF を取り込む（アクティブでなければ何もしない）
    ///
    /// アクティブになる前に NavigationStack の path を操作すると、UIKit 側のスタックと
    /// 食い違って新しい Viewer が古い Viewer の下に入ってしまう
    private func importOpenedPdfIfActive() {
        guard scenePhase == .active, let url = openedPdfUrl else { return }
        openedPdfUrl = nil
        // 取り込みを始める前に一覧まで戻す。Android の popBackStack("top") と
        // 同じ順序（戻す → 追加 → 遷移）にそろえる
        path.removeAll()
        guard let file = preparePdfForImport(from: url) else { return }
        // 他アプリから開かれた PDF は、追加後そのまま Viewer を表示する
        addPdf(file, openAfterAdd: true)
    }

    private func addPdf(_ file: PdfImportFile, openAfterAdd: Bool) {
        pdfAddBridge.add(
            sourceUrl: file.sourceUrl,
            name: file.name,
            createdAt: file.createdAt,
            size: file.size,
            openAfterAdd: openAfterAdd
        )
    }
}
