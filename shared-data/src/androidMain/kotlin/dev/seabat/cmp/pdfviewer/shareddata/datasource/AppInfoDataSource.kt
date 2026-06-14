package dev.seabat.cmp.pdfviewer.shareddata.datasource

actual class AppInfoDataSource actual constructor() : AppInfoDataSourceContract {
    override fun getVersionName(): String = "1.0"
}
