package dev.seabat.cmp.pdfviewer.shareddata.datasource

import dev.seabat.cmp.pdfviewer.shareddata.BuildConfig

actual class AppInfoDataSource actual constructor() : AppInfoDataSourceContract {
    override fun getVersionName(): String = BuildConfig.VERSION_NAME
}
