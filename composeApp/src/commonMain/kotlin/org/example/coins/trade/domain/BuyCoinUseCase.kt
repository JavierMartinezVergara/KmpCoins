package org.example.coins.trade.domain

import kotlinx.coroutines.flow.first
import org.example.coins.core.domain.DataError
import org.example.coins.core.domain.EmptyResult
import org.example.coins.core.domain.Result
import org.example.coins.core.domain.coin.Coin
import org.example.coins.portfolio.domain.PortfolioRepository
import org.example.coins.portfolio.domain.model.PortfolioCoinModel

class BuyCoinUseCase(
    private val portfolioRepository: PortfolioRepository
) {
    suspend fun buyCoin(
        coin: Coin,
        amountInFiat: Double,
        price: Double
    ): EmptyResult<DataError> {
        val balance = portfolioRepository.cashBalanceFlow().first()
        if (balance < amountInFiat) {
            return Result.Error(DataError.Local.INSUFFICIENT_FUNDS)
        }
        val existingCoinResult = portfolioRepository.getPortfolioCoin(coinId = coin.id)
        val existingCoin = when (existingCoinResult) {
            is Result.Error -> return Result.Error(existingCoinResult.error)
            is Result.Success -> existingCoinResult.data
        }
        val amountInUnit = amountInFiat / price
        if (existingCoin != null) {
            val newAmountOwned = existingCoin.ownedAmountUnit + amountInUnit
            val newTotalInvesment = existingCoin.ownedAmountFiat + amountInFiat
            val newAveragePurchasePrice = newTotalInvesment / newAmountOwned
            portfolioRepository.savePortfolioCoin(
                existingCoin.copy(
                    ownedAmountFiat = newTotalInvesment,
                    ownedAmountUnit = newAmountOwned,
                    averagePurchasePrice = newAveragePurchasePrice
                )
            )
        } else {
            portfolioRepository.savePortfolioCoin(
                PortfolioCoinModel(
                    coin = coin,
                    performancePercent = 0.0,
                    averagePurchasePrice = price,
                    ownedAmountFiat = amountInFiat,
                    ownedAmountUnit = amountInUnit
                )
            )
        }
        portfolioRepository.updateCashBalance(balance - amountInFiat)
        return Result.Success(Unit)
    }
}