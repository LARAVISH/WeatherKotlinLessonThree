package com.githab.laravish.weatherkotlinlessonthree.di

import androidx.room.Room
import com.githab.laravish.weatherkotlinlessonthree.domani.Repository
import com.githab.laravish.weatherkotlinlessonthree.domani.RepositoryImplement
import com.githab.laravish.weatherkotlinlessonthree.room.HistoryDatabase
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.DetailsViewModel
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.HistoryViewModel
import com.githab.laravish.weatherkotlinlessonthree.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(androidContext(),
            HistoryDatabase::class.java, "app_database.db").build()
    }
    single<Repository> { RepositoryImplement(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
}