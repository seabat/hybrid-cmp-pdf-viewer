import SwiftUI
import SharedUI

@main
struct iOSApp: App {
    /// 他アプリの共有シート・「このアプリで開く」から渡された PDF の URL
    /// 取り込みが済むと TopScreen 側で nil に戻される
    @State private var openedPdfUrl: URL?

    var body: some Scene {
        WindowGroup {
            NavigationView(openedPdfUrl: $openedPdfUrl)
                .onOpenURL { url in
                    openedPdfUrl = url
                }
        }
    }

    init() {
        AppModuleKt.doInitIosKoin()
    }
}
