package dev.seabat.cmp.pdfviewer.di

import dev.seabat.cmp.pdfviewer.screen.information.InformationViewModel
import dev.seabat.cmp.pdfviewer.screen.top.TopViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { InformationViewModel(get()) }
        viewModel { TopViewModel(get(), get()) }
    }