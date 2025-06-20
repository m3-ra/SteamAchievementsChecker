package com.jc.steamachievementschecker.core

import org.junit.Assert.assertEquals
import org.junit.Test

class SortGameInfoUseCaseTest {

    val useCase = SortGameInfoUseCase()

    @Test
    fun `SHOULD sort game WHEN use case is called`() {
        // Arrange
        val games = listOf(
            GameInfo(1, "Game xyz", 50, "Game xyz"),
            GameInfo(2, "Game abc", 100, "Game abc"),
            GameInfo(3, "Game def", 50, "Game def"),
            GameInfo(4, "The Game", 25, "The Game"),
            GameInfo(5, "Lll Game", 25, "Lll Game")
        )

        // Act
        val result = useCase(games)

        // Assert
        val expected = listOf(
            GameInfo(2, "Game abc", 100, "Game abc"),
            GameInfo(3, "Game def", 50, "Game def"),
            GameInfo(1, "Game xyz", 50, "Game xyz"),
            GameInfo(4, "The Game", 25, "Game"),
            GameInfo(5, "Lll Game", 25, "Lll Game")
        )
        assertEquals(expected, result)
    }
}
