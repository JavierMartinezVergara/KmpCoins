package org.example.coins.coins.presentation

import androidx.compose.runtime.Stable
import org.example.coins.coins.presentation.model.UiCoinListItem
import org.jetbrains.compose.resources.StringResource

@Stable
data class CoinsState(
    val error: StringResource? = null,
    val coins: List<UiCoinListItem> = emptyList(),
    val chartState: UiChartState? = null
)

@Stable
data class UiChartState(
    val sparkLine: List<Double> = emptyList(),
    val isLoading: Boolean = false,
    val coinName: String = "",
)