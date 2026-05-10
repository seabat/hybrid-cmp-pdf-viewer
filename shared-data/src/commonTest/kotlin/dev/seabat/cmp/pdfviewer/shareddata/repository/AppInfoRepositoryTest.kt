package dev.seabat.cmp.pdfviewer.shareddata.repository

import dev.seabat.cmp.pdfviewer.shareddata.datasource.FakeAppInfoDataSource
import kotlin.test.Test
import kotlin.test.assertEquals

class AppInfoRepositoryTest {
    @Test
    fun testGetVersionName() {
        val repository = AppInfoRepository(FakeAppInfoDataSource())
        val versionName = repository.getVersionName()
        assertEquals(true, versionName.isNotEmpty())
    }
}
