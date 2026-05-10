package dev.seabat.cmp.pdfviewer.shareddata.di

import dev.seabat.cmp.pdfviewer.shareddata.repository.PlatformRepository
import dev.seabat.cmp.pdfviewer.shareddata.datasource.PlatformInfoDataSource
import dev.seabat.cmp.pdfviewer.shareddata.datasource.PlatformInfoDataSourceContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PlatformRepositoryContract
import org.koin.dsl.module

// inject を実行するクラスを定義する
val repositoryModule = module {
    single<PlatformRepositoryContract> { PlatformRepository(get()) }
}

val dataSourceModule = module {
    single<PlatformInfoDataSourceContract> { PlatformInfoDataSource() }
}
