package org.example.coins.coins.data.remote.dto

import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
data class CoinDetailsResponseDto(
    val data: CoinResponseDto,
)

@Serializable
data class CoinResponseDto(
    val coin: CoinItemDto,
)