package dev.seabat.cmp.pdfviewer.shareddomain.usecase

interface CreatePhrasesUseCaseContract {
    suspend operator fun invoke(): List<String>
}