package dev.seabat.cmp.pdfviewer.shareddata.di

import dev.seabat.cmp.pdfviewer.shareddata.source.AndroidPlatform
import dev.seabat.cmp.pdfviewer.shareddata.source.PlatformContract
import org.koin.dsl.module

actual val platformModule = module {
    single<PlatformContract> {
        AndroidPlatform()
    }
}