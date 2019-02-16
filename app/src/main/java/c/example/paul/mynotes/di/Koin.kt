package c.example.paul.mynotes.di

import c.example.paul.mynotes.viewmodel.NotesViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    viewModel { NotesViewModel(get())}
}