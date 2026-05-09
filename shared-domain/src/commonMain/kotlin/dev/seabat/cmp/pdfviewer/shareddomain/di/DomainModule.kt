package dev.seabat.cmp.pdfviewer.shareddomain.di

import dev.seabat.cmp.pdfviewer.shareddomain.usecase.CreatePhrasesUseCase
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.CreatePhrasesUseCaseContract
import org.koin.dsl.module

// inject を実行するクラスを定義する
val useCaseModule = module {
    single<CreatePhrasesUseCaseContract> { CreatePhrasesUseCase(get()) }
}
