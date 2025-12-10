package org.example.coins

import androidx.compose.ui.window.ComposeUIViewController
import org.example.coins.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }, content = {
        App()
    })