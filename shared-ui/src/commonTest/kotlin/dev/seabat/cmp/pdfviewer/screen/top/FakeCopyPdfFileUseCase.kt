package dev.seabat.cmp.pdfviewer.screen.top

import dev.seabat.cmp.pdfviewer.shareddomain.usecase.CopyPdfFileUseCaseContract

class FakeCopyPdfFileUseCase : CopyPdfFileUseCaseContract {
    override suspend fun invoke(sourceUri: String, destFileName: String): String = "/fake/path/$destFileName"
}
