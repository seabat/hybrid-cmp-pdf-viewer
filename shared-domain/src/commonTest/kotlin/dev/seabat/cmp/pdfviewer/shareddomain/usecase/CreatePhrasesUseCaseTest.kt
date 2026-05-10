package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.repository.FakeAppInfoRepository
import dev.seabat.cmp.pdfviewer.shareddomain.repository.FakePlatformRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class CreatePhrasesUseCaseTest {
    private lateinit var useCase: CreatePhrasesUseCaseContract

    @Test
    fun testCreatePhrasesUseCase() {
        useCase = CreatePhrasesUseCase(FakePlatformRepository(), FakeAppInfoRepository())

        runTest {
            val phrases = useCase()
            assertEquals(2, phrases.size)
            assertEquals(true, phrases[0].startsWith("OS: Android 34!"))
            assertEquals("Version: 1.0", phrases[1])
        }
    }
}