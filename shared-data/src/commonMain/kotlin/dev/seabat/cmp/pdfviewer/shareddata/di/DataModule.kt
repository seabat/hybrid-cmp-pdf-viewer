package dev.seabat.cmp.pdfviewer.shareddata.di

import dev.seabat.cmp.pdfviewer.shareddata.datasource.AppInfoDataSource
import dev.seabat.cmp.pdfviewer.shareddata.datasource.AppInfoDataSourceContract
import dev.seabat.cmp.pdfviewer.shareddata.datasource.PdfFileDataSource
import dev.seabat.cmp.pdfviewer.shareddata.datasource.PdfFileDataSourceContract
import dev.seabat.cmp.pdfviewer.shareddata.datasource.PdfListDataSource
import dev.seabat.cmp.pdfviewer.shareddata.datasource.PdfListDataSourceContract
import dev.seabat.cmp.pdfviewer.shareddata.datasource.PlatformInfoDataSource
import dev.seabat.cmp.pdfviewer.shareddata.datasource.PlatformInfoDataSourceContract
import dev.seabat.cmp.pdfviewer.shareddata.repository.AppInfoRepository
import dev.seabat.cmp.pdfviewer.shareddata.repository.PdfFileRepository
import dev.seabat.cmp.pdfviewer.shareddata.repository.PdfListRepository
import dev.seabat.cmp.pdfviewer.shareddata.repository.PlatformRepository
import dev.seabat.cmp.pdfviewer.shareddomain.repository.AppInfoRepositoryContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfFileRepositoryContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfListRepositoryContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PlatformRepositoryContract
import org.koin.dsl.module

// inject を実行するクラスを定義する
val repositoryModule = module {
    single<PlatformRepositoryContract> { PlatformRepository(get()) }
    single<AppInfoRepositoryContract> { AppInfoRepository(get()) }
    single<PdfListRepositoryContract> { PdfListRepository(get()) }
    single<PdfFileRepositoryContract> { PdfFileRepository(get()) }
}

val dataSourceModule = module {
    single<PlatformInfoDataSourceContract> { PlatformInfoDataSource() }
    single<AppInfoDataSourceContract> { AppInfoDataSource() }
    single<PdfListDataSourceContract> { PdfListDataSource() }
    single<PdfFileDataSourceContract> { PdfFileDataSource() }
}
