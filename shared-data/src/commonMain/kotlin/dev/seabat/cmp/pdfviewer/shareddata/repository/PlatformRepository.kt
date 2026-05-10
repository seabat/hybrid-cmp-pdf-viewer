package dev.seabat.cmp.pdfviewer.shareddata.repository

import dev.seabat.cmp.pdfviewer.shareddata.datasource.PlatformInfoDataSourceContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PlatformRepositoryContract

class PlatformRepository(private val platformSource: PlatformInfoDataSourceContract) :
    PlatformRepositoryContract {
    override fun getPlatformName(): String = platformSource.getPlatformName()
}