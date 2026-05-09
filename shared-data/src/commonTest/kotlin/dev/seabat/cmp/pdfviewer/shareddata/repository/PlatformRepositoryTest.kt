package dev.seabat.cmp.pdfviewer.shareddata.repository

import dev.seabat.cmp.pdfviewer.shareddata.source.FakePlatformSource
import kotlin.test.Test
import kotlin.test.assertEquals

class PlatformRepositoryTest {
    @Test
    fun testGetPlatformName() {
        val repository = PlatformRepository(FakePlatformSource())
        val platformName = repository.getPlatformName()
        assertEquals(true, platformName.isNotEmpty())
    }
}