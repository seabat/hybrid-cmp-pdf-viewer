import SwiftUI
import ComposeApp

/// Kotlin の AppColor を SwiftUI の Color に変換する拡張
extension Color {
    init(_ appColor: AppColor) {
        self.init(
            red: Double(appColor.red),
            green: Double(appColor.green),
            blue: Double(appColor.blue),
            opacity: Double(appColor.alpha)
        )
    }
}
