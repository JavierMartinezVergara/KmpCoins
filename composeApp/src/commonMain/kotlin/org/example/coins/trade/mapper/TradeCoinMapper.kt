package org.example.coins.trade.mapper

import org.example.coins.core.domain.coin.Coin
import org.example.coins.trade.presentation.common.UiTradeCoinItem

fun UiTradeCoinItem.toCoin() : Coin {
    return Coin(
        id = id,
        name = name,
        symbol = symbol,
        iconUrl = iconUrl
    )
}