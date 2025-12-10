package org.example.coins.di

import androidx.room.RoomDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import org.example.coins.core.database.portfolio.PortfolioDatabase
import org.example.coins.core.database.portfolio.getPortfolioDatabaseBuilder
import org.example.coins.core.network.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<HttpClient> { HttpClientFactory.create(Android.create()) }
    singleOf(::getPortfolioDatabaseBuilder).bind<RoomDatabase.Builder<PortfolioDatabase>>()
}