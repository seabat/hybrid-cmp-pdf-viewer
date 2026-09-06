package dev.seabat.cmp.pdfviewer.screen.top

import dev.seabat.cmp.pdfviewer.shareddomain.usecase.DeletePdfFileUseCaseContract

class FakeDeletePdfFileUseCase : DeletePdfFileUseCaseContract {
    var deletedFilePath: String? = null
        private set

    override suspend fun invoke(filePath: String) {
        deletedFilePath = filePath
    }
}
