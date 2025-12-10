package org.example.coins.coins.domain.api

import org.example.coins.coins.data.remote.dto.CoinDetailsResponseDto
import org.example.coins.coins.data.remote.dto.CoinPriceHistoryResponseDto
import org.example.coins.coins.data.remote.dto.CoinsResponseDto
import org.example.coins.core.domain.DataError
import org.example.coins.core.domain.Result

interface CoinsRemoteDataSource {

    suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote>

    suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote>

    suspend fun getCoinById(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote>
}