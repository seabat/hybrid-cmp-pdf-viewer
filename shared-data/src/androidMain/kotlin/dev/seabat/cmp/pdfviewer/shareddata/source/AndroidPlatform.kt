package dev.seabat.cmp.pdfviewer.shareddata.source

import android.os.Build

class AndroidPlatform : PlatformContract {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}