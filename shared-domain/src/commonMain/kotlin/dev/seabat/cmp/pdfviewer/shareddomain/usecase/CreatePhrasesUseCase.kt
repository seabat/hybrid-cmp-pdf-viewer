package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.repository.AppInfoRepositoryContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PlatformRepositoryContract

class CreatePhrasesUseCase(
    private val platformRepository: PlatformRepositoryContract,
    private val appInfoRepository: AppInfoRepositoryContract
) : CreatePhrasesUseCaseContract {
    override suspend fun invoke(): List<String> = buildList {
        add("OS: ${platformRepository.getPlatformName()}!")
        add("Version: ${appInfoRepository.getVersionName()}")
    }
}