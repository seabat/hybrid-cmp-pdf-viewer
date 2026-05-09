package dev.seabat.cmp.pdfviewer.di

import dev.seabat.cmp.pdfviewer.screen.viewer.ViewerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { ViewerViewModel(get()) }
    }