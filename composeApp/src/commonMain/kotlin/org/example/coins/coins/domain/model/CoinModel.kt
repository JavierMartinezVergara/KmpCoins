package org.example.coins.coins.domain.model

import org.example.coins.core.domain.coin.Coin

data class CoinModel(
    val coin: Coin,
    val price: Double,
    val change: Double,
)