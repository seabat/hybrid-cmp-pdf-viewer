package dev.seabat.cmp.pdfviewer.shareddata.di

import dev.seabat.cmp.pdfviewer.shareddata.source.IOSPlatform
import dev.seabat.cmp.pdfviewer.shareddata.source.PlatformContract
import org.koin.dsl.module

actual val platformModule = module {
    single<PlatformContract> {
        IOSPlatform()
    }
}