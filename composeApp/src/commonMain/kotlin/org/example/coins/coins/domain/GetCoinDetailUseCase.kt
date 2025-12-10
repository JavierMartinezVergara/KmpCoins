package org.example.coins.coins.domain


import org.example.coins.coins.data.mapper.toCoinModel
import org.example.coins.coins.domain.api.CoinsRemoteDataSource
import org.example.coins.coins.domain.model.CoinModel
import org.example.coins.core.domain.DataError
import org.example.coins.core.domain.Result
import org.example.coins.core.domain.map

class GetCoinDetailUseCase(
    private val client: CoinsRemoteDataSource
) {
    suspend fun execute(coinId: String): Result<CoinModel, DataError.Remote> {
        return client.getCoinById(coinId).map { dto ->
            dto.data.coin.toCoinModel()
        }
    }
}