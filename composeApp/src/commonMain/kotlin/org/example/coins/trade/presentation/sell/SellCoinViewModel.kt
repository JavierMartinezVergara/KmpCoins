package org.example.coins.trade.presentation.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.coins.coins.domain.GetCoinDetailUseCase
import org.example.coins.core.domain.Result
import org.example.coins.core.util.formatFiat
import org.example.coins.core.util.toUiText
import org.example.coins.portfolio.domain.PortfolioRepository
import org.example.coins.trade.domain.SellCoinUseCase
import org.example.coins.trade.mapper.toCoin
import org.example.coins.trade.presentation.common.TradeState
import org.example.coins.trade.presentation.common.UiTradeCoinItem

class SellCoinViewModel(
    private val getCoinDetailsUseCase: GetCoinDetailUseCase,
    private val portfolioRepository: PortfolioRepository,
    private val sellCoinUseCase: SellCoinUseCase
) : ViewModel() {

    private val tempCoinId = "1"
    private val _amount = MutableStateFlow("")
    private val _state = MutableStateFlow(TradeState())
    val state = combine(
        _state, _amount
    ) { state, amount ->
        state.copy(
            amount = amount
        )
    }.onStart {
        when (val portFolioCoinResponse = portfolioRepository.getPortfolioCoin(tempCoinId)) {
            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = portFolioCoinResponse.error.toUiText()
                    )
                }
            }

            is Result.Success -> {
                portFolioCoinResponse.data?.ownedAmountUnit?.let {
                    getCoinsDetails(it)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TradeState(isLoading = true)
    )

    fun onAmountChanged(amount: String) {
        _amount.value = amount
    }

    private suspend fun getCoinsDetails(ownedAmountInPrice: Double) {
        when(val coinResponse = getCoinDetailsUseCase.execute(tempCoinId)) {
            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = coinResponse.error.toUiText()
                    )
                }
            }
            is Result.Success -> {
                val availableAmountInFiat = coinResponse.data.price * ownedAmountInPrice
                _state.update {
                    it.copy(
                        coin = UiTradeCoinItem(
                            id = coinResponse.data.coin.id,
                            name = coinResponse.data.coin.name,
                            symbol = coinResponse.data.coin.symbol,
                            iconUrl = coinResponse.data.coin.iconUrl,
                            price = coinResponse.data.price
                        ),
                        amount = "Available ${formatFiat(availableAmountInFiat)}"
                    )
                }
            }
        }

    }

    fun onSellClicked(){
        val tradeCoin = _state.value.coin ?: return
        viewModelScope.launch {
            val sellCoinResponse = sellCoinUseCase.sellCoin(
                coin = tradeCoin.toCoin(),
                amountInFiat = _amount.value.toDouble(),
                price = tradeCoin.price
            )
            when(sellCoinResponse){
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = sellCoinResponse.error.toUiText()
                        )
                    }
                }
                is Result.Success -> TODO()
            }
        }
    }
}