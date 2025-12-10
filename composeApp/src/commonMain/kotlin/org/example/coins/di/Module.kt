package org.example.coins.di

import androidx.room.RoomDatabase
import io.ktor.client.HttpClient
import org.example.coins.coins.data.remote.impl.KtorCoinsRemoteDataSource
import org.example.coins.coins.domain.GetCoinsListUseCase
import org.example.coins.coins.domain.GetCoinDetailUseCase
import org.example.coins.coins.domain.GetCoinPriceHistoryUseCase
import org.example.coins.coins.domain.api.CoinsRemoteDataSource
import org.example.coins.coins.presentation.viewmodel.CoinsListViewModel
import org.example.coins.core.database.portfolio.PortfolioDatabase
import org.example.coins.core.database.portfolio.getPortfolioDatabase
import org.example.coins.core.network.HttpClientFactory
import org.example.coins.portfolio.data.PortfolioRepositoryImpl
import org.example.coins.portfolio.domain.PortfolioRepository
import org.example.coins.portfolio.presentation.PortfolioViewModel
import org.example.coins.trade.domain.BuyCoinUseCase
import org.example.coins.trade.domain.SellCoinUseCase
import org.example.coins.trade.presentation.buy.BuyCoinViewModel
import org.example.coins.trade.presentation.sell.SellCoinViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            platformModule
        )
    }
}

expect val platformModule: Module

val sharedModule = module {
    single<HttpClient> {
        HttpClientFactory.create(get())
    }


    single {
        getPortfolioDatabase(get<RoomDatabase.Builder<PortfolioDatabase>>())
    }


    singleOf(::PortfolioRepositoryImpl).bind<PortfolioRepository>()
    single { get<PortfolioDatabase>().portfolioDao() }
    single { get<PortfolioDatabase>().userBalanceDao() }
    viewModel { BuyCoinViewModel(get(), get(), get()) }
    viewModel { SellCoinViewModel(get(), get(), get()) }
    viewModel { PortfolioViewModel(get()) }
    viewModel { CoinsListViewModel(get(), get()) }
    singleOf(::GetCoinsListUseCase)
    singleOf(::KtorCoinsRemoteDataSource).bind<CoinsRemoteDataSource>()
    singleOf(::GetCoinDetailUseCase)
    singleOf(::GetCoinPriceHistoryUseCase)
    singleOf(::BuyCoinUseCase)
    singleOf(::SellCoinUseCase)

    //trade

}

