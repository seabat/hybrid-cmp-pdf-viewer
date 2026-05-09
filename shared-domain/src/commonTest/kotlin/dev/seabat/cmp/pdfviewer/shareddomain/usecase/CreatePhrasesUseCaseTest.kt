package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.repository.FakePlatformRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class CreatePhrasesUseCaseTest {
    private lateinit var useCase: CreatePhrasesUseCaseContract

    @Test
    fun testCreatePhrasesUseCase() {
        useCase = CreatePhrasesUseCase(FakePlatformRepository())

        runTest {
            val phrases = useCase()
            assertEquals(phrases.size, 1)
            assertEquals(true, phrases[0].startsWith("Hello, Android 34!"))
        }
    }
}