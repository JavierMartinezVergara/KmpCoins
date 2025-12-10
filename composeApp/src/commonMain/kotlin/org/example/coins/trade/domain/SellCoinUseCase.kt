package org.example.coins.trade.domain

import kotlinx.coroutines.flow.first
import org.example.coins.core.domain.DataError
import org.example.coins.core.domain.EmptyResult
import org.example.coins.core.domain.Result
import org.example.coins.core.domain.coin.Coin
import org.example.coins.portfolio.domain.PortfolioRepository

class SellCoinUseCase(
    private val portfolioRepository: PortfolioRepository
) {

    suspend fun sellCoin(
        coin: Coin,
        amountInFiat: Double,
        price: Double
    ): EmptyResult<DataError> {
        val sellAllThreshold = 1
        when (val existingCoinResponse = portfolioRepository.getPortfolioCoin(coinId = coin.id)) {
            is Result.Error -> return existingCoinResponse
            is Result.Success -> {
                val existingCoin = existingCoinResponse.data
                val sellAmountInUnit = amountInFiat / price
                val balance = portfolioRepository.cashBalanceFlow().first()
                if (existingCoin == null || existingCoin.ownedAmountUnit < sellAmountInUnit) {
                    return Result.Error(DataError.Local.INSUFFICIENT_FUNDS)
                }
                val remainingAmountFiat = existingCoin.ownedAmountFiat - amountInFiat
                val remainingAmountUnit = existingCoin.ownedAmountFiat - sellAmountInUnit
                if (remainingAmountUnit < sellAllThreshold) {
                    portfolioRepository.removeCoinFromPortfolio(coinId = coin.id)
                } else {
                    portfolioRepository.savePortfolioCoin(
                        existingCoin.copy(
                            ownedAmountFiat = remainingAmountFiat,
                            ownedAmountUnit = remainingAmountUnit
                        )
                    )
                }
                portfolioRepository.updateCashBalance(balance + amountInFiat)
                return Result.Success(Unit)
            }
        }
    }
}