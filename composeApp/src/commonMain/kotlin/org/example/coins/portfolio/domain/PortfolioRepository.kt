package org.example.coins.portfolio.domain

import kotlinx.coroutines.flow.Flow
import org.example.coins.core.domain.DataError
import org.example.coins.core.domain.EmptyResult
import org.example.coins.core.domain.Result
import org.example.coins.portfolio.domain.model.PortfolioCoinModel

interface PortfolioRepository {

    suspend fun initializeBalance()

    fun allPortfolioCoins(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>>

    suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel?, DataError.Remote>

    suspend fun savePortfolioCoin(portfolioCoinModel: PortfolioCoinModel): EmptyResult<DataError.Local>

    suspend fun removeCoinFromPortfolio(coinId: String)

    fun calculateTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>>

    fun calculateBalanceFlow(): Flow<Result<Double, DataError.Remote>>

    fun cashBalanceFlow(): Flow<Double>
    suspend fun updateCashBalance(newBalance: Double)
}