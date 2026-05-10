package dev.seabat.cmp.pdfviewer.shareddata.datasource

import platform.Foundation.NSBundle

actual class AppInfoDataSource actual constructor() : AppInfoDataSourceContract {
    override fun getVersionName(): String =
        NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: ""
}
