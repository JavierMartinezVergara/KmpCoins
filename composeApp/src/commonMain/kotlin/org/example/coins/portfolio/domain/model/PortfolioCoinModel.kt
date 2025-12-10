package org.example.coins.portfolio.domain.model

import org.example.coins.core.domain.coin.Coin

data class PortfolioCoinModel(
    val coin: Coin,
    val performancePercent: Double,
    val averagePurchasePrice: Double,
    val ownedAmountUnit: Double,
    val ownedAmountFiat: Double
)