package com.githab.laravish.weatherkotlinlessonthree.viewmodel.koin

import com.githab.laravish.weatherkotlinlessonthree.model.Repository
import com.githab.laravish.weatherkotlinlessonthree.model.RepositoryImplement
import com.githab.laravish.weatherkotlinlessonthree.view.details.DetailsViewModel
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Repository> { RepositoryImplement() }
    viewModel { MainViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}