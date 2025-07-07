package com.jc.steamachievementschecker.core

import org.junit.Assert.assertEquals
import org.junit.Test

class ComputeShortNameUseCaseTest {

    val useCase = ComputeShortNameUseCase()

    @Test
    fun `SHOULD convert to 2 WHEN name has II`() {
        val name = "Baldur's Gate II: Enhanced Edition"
        val result = useCase(name)
        assertEquals("BG2", result)
    }

    @Test
    fun `SHOULD convert to 9 WHEN name has IX`() {
        val name = "Ys IX: Monstrum Nox"
        val result = useCase(name)
        assertEquals("Y9M", result)
    }

    @Test
    fun `SHOULD not crash WHEN name has -`() {
        val name = "Fallout 3 - Game of the Year Edition"
        val result = useCase(name)
        assertEquals("F3G", result)
    }
}
