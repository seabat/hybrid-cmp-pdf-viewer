import SwiftUI
import SharedUI

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