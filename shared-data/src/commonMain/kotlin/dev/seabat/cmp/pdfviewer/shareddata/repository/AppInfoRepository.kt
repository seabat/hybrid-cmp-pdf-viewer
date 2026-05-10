package dev.seabat.cmp.pdfviewer.shareddata.repository

import dev.seabat.cmp.pdfviewer.shareddata.datasource.AppInfoDataSourceContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.AppInfoRepositoryContract

class AppInfoRepository(private val appInfoSource: AppInfoDataSourceContract) :
    AppInfoRepositoryContract {
    override fun getVersionName(): String = appInfoSource.getVersionName()
}
