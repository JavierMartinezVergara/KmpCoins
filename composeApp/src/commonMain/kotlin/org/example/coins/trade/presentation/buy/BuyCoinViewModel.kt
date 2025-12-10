package org.example.coins.trade.presentation.buy


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.coins.coins.domain.GetCoinDetailUseCase
import org.example.coins.core.domain.Result
import org.example.coins.core.util.toUiText
import org.example.coins.portfolio.domain.PortfolioRepository
import org.example.coins.trade.domain.BuyCoinUseCase
import org.example.coins.trade.mapper.toCoin
import org.example.coins.trade.presentation.common.TradeState
import org.example.coins.trade.presentation.common.UiTradeCoinItem

class BuyCoinViewModel(
    private val getCoinDetailsUseCase: GetCoinDetailUseCase,
    private val portfolioRepository: PortfolioRepository,
    private val buyCoinUseCase: BuyCoinUseCase
) : ViewModel() {
    private val tempCoinId = "1"
    private val _amount = MutableStateFlow("")
    private val _state = MutableStateFlow(TradeState())
    val state = combine(
        _state,
        _amount
    ) { state, amount ->
        state.copy(
            amount = amount
        )
    }.onStart {
        val balance = portfolioRepository.cashBalanceFlow().first()
        getCoinsDetails(balance)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TradeState(isLoading = true)
    )

    private suspend fun getCoinsDetails(balance: Double) {
        when (val coinResponse = getCoinDetailsUseCase.execute(tempCoinId)) {
            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = coinResponse.error.toUiText()
                    )
                }
            }

            is Result.Success -> {
                _state.update {
                    it.copy(
                        coin = UiTradeCoinItem(
                            id = coinResponse.data.coin.id,
                            name = coinResponse.data.coin.name,
                            symbol = coinResponse.data.coin.symbol,
                            iconUrl = coinResponse.data.coin.iconUrl,
                            price = coinResponse.data.price
                        )
                    )
                }
            }
        }
    }

    fun onAmountChanged(amount: String) {
        _amount.value = amount
    }

    fun onBuyClicked() {
        val tradeCoin = _state.value.coin ?: return
        viewModelScope.launch {
            val buyCoinResponse = buyCoinUseCase.buyCoin(
                tradeCoin.toCoin(),
                amountInFiat = _amount.value.toDouble(),
                price = tradeCoin.price
            )
            when(buyCoinResponse) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = buyCoinResponse.error.toUiText()
                        )
                    }
                }
                is Result.Success -> TODO()
            }
        }
    }

}