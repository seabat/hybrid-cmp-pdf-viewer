package dev.seabat.cmp.pdfviewer.shareddata.datasource

class FakeAppInfoDataSource : AppInfoDataSourceContract {
    override fun getVersionName() = "1.0"
}
