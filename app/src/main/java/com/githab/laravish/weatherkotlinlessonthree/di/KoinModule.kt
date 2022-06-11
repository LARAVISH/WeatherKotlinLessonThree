package com.githab.laravish.weatherkotlinlessonthree.di

import com.githab.laravish.weatherkotlinlessonthree.domani.Repository
import com.githab.laravish.weatherkotlinlessonthree.domani.RepositoryImplement
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.DetailsViewModel
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Repository> { RepositoryImplement() }
    viewModel { MainViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}