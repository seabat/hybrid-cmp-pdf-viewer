package dev.seabat.cmp.pdfviewer

import android.app.Application
import dev.seabat.cmp.pdfviewer.di.initAndroidKoin
import org.koin.android.ext.koin.androidContext

class PdfViewerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initAndroidKoin {
            androidContext(this@PdfViewerApplication)
        }
    }
}