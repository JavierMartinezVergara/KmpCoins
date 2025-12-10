package org.example.coins.portfolio.presentation

import org.jetbrains.compose.resources.StringResource

data class PortfolioState(
    val portFolioValue: String = "",
    val cashBalance: String = "",
    val showBuyButton: Boolean = false,
    val isLoading: Boolean = false,
    val error: StringResource? = null,
    val coins: List<UiPortfolioCoinItem> = emptyList()
)
