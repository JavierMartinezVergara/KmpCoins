package org.example.coins.portfolio.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import org.example.coins.core.domain.DataError
import org.example.coins.core.domain.Result
import org.example.coins.core.util.formatCoinUnit
import org.example.coins.core.util.formatFiat
import org.example.coins.core.util.formatPercentage
import org.example.coins.core.util.toUiText
import org.example.coins.portfolio.domain.PortfolioRepository
import org.example.coins.portfolio.domain.model.PortfolioCoinModel

class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PortfolioState(isLoading = true))
    val state: StateFlow<PortfolioState> = combine(
        _state,
        portfolioRepository.allPortfolioCoins(),
        portfolioRepository.calculateTotalPortfolioValue(),
        portfolioRepository.cashBalanceFlow()

    ) { currentState, porfolioCoins, totalBalance, cashBalance ->
        when (porfolioCoins) {
            is Result.Error -> handleStateError(currentState, porfolioCoins.error)
            is Result.Success -> handleStateSuccess(
                currentState,
                porfolioCoins.data,
                totalBalance,
                cashBalance
            )
        }
    }.onStart {
        portfolioRepository.initializeBalance()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PortfolioState(isLoading = true)
    )

    private fun handleStateSuccess(
        currentState: PortfolioState,
        portfolioCoins: List<PortfolioCoinModel>,
        totalBalance: Result<Double, DataError>,
        cashBalance: Double
    ): PortfolioState {
        val portFolioValue = when (totalBalance) {
            is Result.Error -> formatFiat(0.0)
            is Result.Success -> formatFiat(totalBalance.data)
        }
        return currentState.copy(
            coins = portfolioCoins.map {
                it.toUiPortfolioCoinItem()
            },
            portFolioValue = portFolioValue,
            cashBalance = formatFiat(cashBalance),
            showBuyButton = portfolioCoins.isNotEmpty(),
            isLoading = false
        )
    }

    private fun handleStateError(
        currentState: PortfolioState,
        error: DataError
    ): PortfolioState {
        return currentState.copy(
            isLoading = false,
            error = error.toUiText()
        )
    }

    private fun PortfolioCoinModel.toUiPortfolioCoinItem(): UiPortfolioCoinItem {
        return UiPortfolioCoinItem(
            id = coin.id,
            name = coin.name,
            iconUrl = coin.iconUrl,
            amountInUnitText = formatCoinUnit(ownedAmountUnit, coin.symbol),
            amountInFiatText = formatFiat(ownedAmountFiat),
            performancePercentText = formatPercentage(performancePercent),
            isPositive = performancePercent >= 0
        )
    }
}