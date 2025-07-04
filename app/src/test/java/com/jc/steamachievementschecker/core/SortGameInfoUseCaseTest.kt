package com.jc.steamachievementschecker.core

import org.junit.Assert.assertEquals
import org.junit.Test

class SortGameInfoUseCaseTest {

    val useCase = SortGameInfoUseCase()

    @Test
    fun `SHOULD sort games WHEN use case is called`() {
        // Arrange
        val games = listOf(
            GameInfo(1, "Game xyz", 50),
            GameInfo(2, "Game abc", 100),
            GameInfo(3, "Game def", 50),
            GameInfo(4, "The Game", 25),
            GameInfo(5, "Lll Game", 25)
        )

        // Act
        val result = useCase(games)

        // Assert
        val expected = listOf(
            GameInfoItem(2, "Game abc", 100, "Game abc", "Ga"),
            GameInfoItem(3, "Game def", 50, "Game def", "Gd"),
            GameInfoItem(1, "Game xyz", 50, "Game xyz", "Gx"),
            GameInfoItem(4, "The Game", 25, "Game", "TG"),
            GameInfoItem(5, "Lll Game", 25, "Lll Game", "LG")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `SHOULD sort games ignoring case WHEN they have capital letters`() {
        // Arrange
        val games = listOf(
            GameInfo(1, "TU", 100),
            GameInfo(2, "Ti", 100),
            GameInfo(3, "Ta", 100),
            GameInfo(4, "TH", 100)
        )

        // Act
        val result = useCase(games)

        // Assert
        val expected = listOf(
            GameInfoItem(3, "Ta", 100, "Ta", "Ta"),
            GameInfoItem(4, "TH", 100, "TH", "TH"),
            GameInfoItem(2, "Ti", 100, "Ti", "Ti"),
            GameInfoItem(1, "TU", 100, "TU", "TU")
        )
        assertEquals(expected, result)
    }
}
