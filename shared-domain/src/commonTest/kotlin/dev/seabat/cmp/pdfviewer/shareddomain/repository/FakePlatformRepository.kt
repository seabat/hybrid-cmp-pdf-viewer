package dev.seabat.cmp.pdfviewer.shareddomain.repository

class FakePlatformRepository : PlatformRepositoryContract {
    override fun getPlatformName(): String = "Android 34"
}