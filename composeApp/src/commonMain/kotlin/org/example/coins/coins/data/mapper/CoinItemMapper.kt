package org.example.coins.coins.data.mapper

import org.example.coins.coins.data.remote.dto.CoinItemDto
import org.example.coins.coins.data.remote.dto.CoinPriceDto
import org.example.coins.core.domain.coin.Coin
import org.example.coins.coins.domain.model.CoinModel
import org.example.coins.coins.domain.model.PriceModel

fun CoinItemDto.toCoinModel() = CoinModel(
    coin = Coin(
        id = uuid,
        name = name,
        symbol = symbol,
        iconUrl = iconUrl,
    ),
    price = price,
    change = change,
)

fun CoinPriceDto.toPriceModel() = PriceModel(
    price = price ?: 0.0,
    timestamp = timestamp
)