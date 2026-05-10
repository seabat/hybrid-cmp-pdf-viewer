package dev.seabat.cmp.pdfviewer.shareddomain.repository

class FakeAppInfoRepository : AppInfoRepositoryContract {
    override fun getVersionName(): String = "1.0"
}
