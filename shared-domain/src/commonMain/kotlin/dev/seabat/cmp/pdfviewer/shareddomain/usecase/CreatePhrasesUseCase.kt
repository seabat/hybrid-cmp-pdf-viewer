package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.repository.PlatformRepositoryContract

class CreatePhrasesUseCase(private val platformRepository: PlatformRepositoryContract) :
    CreatePhrasesUseCaseContract {
    override suspend fun invoke(): List<String> = buildList {
        add("Hello, ${platformRepository.getPlatformName()}!")
    }
}