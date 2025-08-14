package org.example.coins

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform