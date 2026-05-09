package dev.seabat.cmp.pdfviewer.shareddata.di

import dev.seabat.cmp.pdfviewer.shareddata.repository.PlatformRepository
import dev.seabat.cmp.pdfviewer.shareddata.source.PlatformSource
import dev.seabat.cmp.pdfviewer.shareddata.source.PlatformSourceContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PlatformRepositoryContract
import org.koin.dsl.module

// inject を実行するクラスを定義する
val repositoryModule = module {
    single<PlatformSourceContract> { PlatformSource(get()) }
    single<PlatformRepositoryContract> { PlatformRepository(get()) }
}
