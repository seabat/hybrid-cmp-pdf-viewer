package dev.seabat.cmp.pdfviewer.di

import dev.seabat.cmp.pdfviewer.shareddata.di.dataSourceModule
import dev.seabat.cmp.pdfviewer.shareddata.di.repositoryModule
import dev.seabat.cmp.pdfviewer.shareddomain.di.useCaseModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

// for Android
fun initAndroidKoin(appDeclaration: KoinAppDeclaration) = startKoin {
    appDeclaration()
    modules(
        useCaseModule,
        viewModelModule,
        repositoryModule,
        dataSourceModule
    )
}

// for Compose Preview
fun initComposePreviewKoin() = startKoin {
    modules(
        viewModelModule,
        useCaseModule,
        repositoryModule,
        dataSourceModule
    )
}

// for iOS
// Kotlin の関数は  [ファイル名]Kt.do[関数名]() として Swift から呼び出す。
// CommonModuleKt.doInitKoin()
fun initIosKoin() {
    startKoin {
        modules(
            useCaseModule,
            viewModelModule,
            repositoryModule,
            dataSourceModule
        )
    }
}