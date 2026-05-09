import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            NavigationView()
        }
    }

    init() {
        AppModuleKt.doInitIosKoin()
    }
}