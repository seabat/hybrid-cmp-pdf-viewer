package dev.seabat.cmp.pdfviewer.shareddata.datasource

import platform.UIKit.UIDevice

actual class PlatformInfoDataSource actual constructor() : PlatformInfoDataSourceContract {
    override fun getPlatformName(): String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}
