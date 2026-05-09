package dev.seabat.cmp.pdfviewer.shareddata.source

class PlatformSource(private val platform: PlatformContract) : PlatformSourceContract {
    override fun getPlatformName(): String = platform.name
}