package dev.seabat.cmp.pdfviewer.shareddata.datasource

import android.os.Build

actual class PlatformInfoDataSource actual constructor() : PlatformInfoDataSourceContract {
    override fun getPlatformName(): String = "Android ${Build.VERSION.SDK_INT}"
}
