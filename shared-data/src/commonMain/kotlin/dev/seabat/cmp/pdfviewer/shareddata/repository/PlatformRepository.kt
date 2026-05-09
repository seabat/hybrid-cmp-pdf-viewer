package dev.seabat.cmp.pdfviewer.shareddata.repository

import dev.seabat.cmp.pdfviewer.shareddata.source.PlatformSourceContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PlatformRepositoryContract

class PlatformRepository(private val platformSource: PlatformSourceContract) :
    PlatformRepositoryContract {
    override fun getPlatformName(): String = platformSource.getPlatformName()
}