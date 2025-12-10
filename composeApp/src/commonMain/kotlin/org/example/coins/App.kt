package org.example.coins

import androidx.compose.runtime.Composable
import org.example.coins.coins.presentation.ui.CoinsListScreen
import org.example.coins.portfolio.presentation.PortfolioScreen
import org.example.coins.theme.CoinRoutineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    CoinRoutineTheme {
        PortfolioScreen({}, {})
    }
}