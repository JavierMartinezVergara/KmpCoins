package org.example.coins.coins.domain

import org.example.coins.coins.data.mapper.toPriceModel
import org.example.coins.coins.domain.api.CoinsRemoteDataSource
import org.example.coins.coins.domain.model.PriceModel
import org.example.coins.core.domain.DataError
import org.example.coins.core.domain.Result
import org.example.coins.core.domain.map

class GetCoinPriceHistoryUseCase(
    private val client: CoinsRemoteDataSource
) {

    suspend fun execute(coinId: String): Result<List<PriceModel>, DataError.Remote> {
        return client.getPriceHistory(coinId).map { coinPrice ->
            coinPrice.data.history.map {
                it.toPriceModel()
            }

        }
    }
}