package org.example.coins.portfolio.data.mapper

import kotlinx.datetime.Clock
import org.example.coins.core.domain.coin.Coin
import org.example.coins.portfolio.data.local.PortfolioCoinEntity
import org.example.coins.portfolio.domain.model.PortfolioCoinModel

fun PortfolioCoinEntity.toPortfolioCoinModel(
    currentPrice: Double
): PortfolioCoinModel {
    return PortfolioCoinModel(
        coin = Coin(
            id = coinId,
            name = name,
            symbol = symbol,
            iconUrl = iconUrl
        ),
        performancePercent = ((currentPrice - averagePurchasePrice) / averagePurchasePrice) * 100,
        averagePurchasePrice = averagePurchasePrice,
        ownedAmountUnit = amountOwned,
        ownedAmountFiat = amountOwned * currentPrice
    )
}

fun PortfolioCoinModel.toPortfolioCoinEntity() : PortfolioCoinEntity {
    return PortfolioCoinEntity(
        coinId = coin.id,
        name = coin.name,
        symbol = coin.symbol,
        iconUrl = coin.iconUrl,
        amountOwned = ownedAmountUnit,
        averagePurchasePrice = averagePurchasePrice,
        timeStamp = Clock.System.now().toEpochMilliseconds()
    )
}