package org.example.coins.core.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun formatFiat(amount: Double, showDecimal: Boolean): String {
    val numberFormatter = NSNumberFormatter()
    numberFormatter.numberStyle = NSNumberFormatterDecimalStyle

    when {
        showDecimal.not() -> {
            numberFormatter.minimumFractionDigits.apply { 0.toLong() }
            numberFormatter.maximumFractionDigits.apply { 0.toLong() }
        }

        amount >= 0.01 -> {
            numberFormatter.minimumFractionDigits.apply { 2.toLong() }
            numberFormatter.maximumFractionDigits.apply { 2.toLong() }
        }

        else -> {
            numberFormatter.minimumFractionDigits.apply { 8.toLong() }
            numberFormatter.maximumFractionDigits .apply { 8.toLong() }
        }
    }

    val formatterAmount = numberFormatter.stringFromNumber(NSNumber(amount))
    return if (formatterAmount != null) "$ $formatterAmount" else ""
}

actual fun formatCoinUnit(amount: Double, symbol: String): String {
    val numberFormatter = NSNumberFormatter()
    numberFormatter.numberStyle = NSNumberFormatterDecimalStyle
    numberFormatter.minimumFractionDigits.apply { 8.toLong() }
    numberFormatter.maximumFractionDigits.apply { 8.toLong() }

    return numberFormatter.stringFromNumber(NSNumber(amount)) + " $symbol"
}

actual fun formatPercentage(amount: Double): String {
    val numberFormatter = NSNumberFormatter()
    numberFormatter.numberStyle = NSNumberFormatterDecimalStyle
    numberFormatter.minimumFractionDigits.apply { 2.toLong() }
    numberFormatter.maximumFractionDigits.apply { 2.toLong() }
    val prefix = if (amount >= 0) "+" else ""

    return prefix + numberFormatter.stringFromNumber(NSNumber(amount)) + " %"
}