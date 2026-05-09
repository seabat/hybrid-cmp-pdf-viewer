package dev.seabat.cmp.pdfviewer.theme

import androidx.compose.ui.graphics.Color

/**
 * アプリ全体で使用するカラー定義
 * 16進数 ARGB 形式 (0xAARRGGBBu) で色を定義する
 * - Compose: AppColors.xxx.toComposeColor() で参照
 * - iOS Swift: AppColors.shared.xxx で参照
 */
data class AppColor(val argbHex: UInt) {
    val alpha: Float = ((argbHex shr 24) and 0xFFu).toFloat() / 255f
    val red: Float   = ((argbHex shr 16) and 0xFFu).toFloat() / 255f
    val green: Float = ((argbHex shr 8)  and 0xFFu).toFloat() / 255f
    val blue: Float  = (argbHex          and 0xFFu).toFloat() / 255f

    fun toComposeColor(): Color = Color(argbHex.toInt())
}

object AppColors {
    val headerContainer  = AppColor(0xFF00FF00u)  // 純緑
    val contentContainer = AppColor(0xFFFF00FFu)  // マゼンタ
}
