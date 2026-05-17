package dev.seabat.cmp.pdfviewer.shareddomain.di

import dev.seabat.cmp.pdfviewer.shareddomain.usecase.CreatePhrasesUseCase
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.CreatePhrasesUseCaseContract
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.ReadPdfListUseCase
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.ReadPdfListUseCaseContract
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.SavePdfListUseCase
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.SavePdfListUseCaseContract
import org.koin.dsl.module

// inject を実行するクラスを定義する
val useCaseModule = module {
    single<CreatePhrasesUseCaseContract> { CreatePhrasesUseCase(get(), get()) }
    single<SavePdfListUseCaseContract> { SavePdfListUseCase(get()) }
    single<ReadPdfListUseCaseContract> { ReadPdfListUseCase(get()) }
}
