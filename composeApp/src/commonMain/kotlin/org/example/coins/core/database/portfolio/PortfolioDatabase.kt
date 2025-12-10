package org.example.coins.core.database.portfolio

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.coins.portfolio.data.local.PortfolioCoinEntity
import org.example.coins.portfolio.data.local.PortfolioDao
import org.example.coins.portfolio.data.local.UserBalanceDao
import org.example.coins.portfolio.data.local.UserBalanceEntity


@ConstructedBy(PortfolioDatabaseCreator::class)
@Database(entities = [PortfolioCoinEntity::class, UserBalanceEntity::class], version = 2)
abstract class PortfolioDatabase : RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao
    abstract fun userBalanceDao(): UserBalanceDao
}