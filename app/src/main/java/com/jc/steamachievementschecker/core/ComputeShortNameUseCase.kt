package com.jc.steamachievementschecker.core

private const val SHORT_DISPLAY_MAX_LENGTH = 3

class ComputeShortNameUseCase {

    operator fun invoke(name: String): String {
        val split = name.split(" ")
        val lettersDigitsSplit = split
            .map { it.filter { char -> char.isLetterOrDigit() } }
            .filter { it.isNotEmpty() }

        return if (lettersDigitsSplit.size > 1) {
            lettersDigitsSplit
                .map {
                    if (it.all { char -> char in romanMap.keys }) {
                        romanToInt(it)
                    } else {
                        it.first()
                    }
                }.joinToString("")
                .take(SHORT_DISPLAY_MAX_LENGTH)
        } else {
            name.take(SHORT_DISPLAY_MAX_LENGTH)
        }
    }

    private fun romanToInt(s: String): Int {
        var sum = 0
        var prevValue = romanMap[s[0]] ?: 0

        for (i in 1 until s.length) {
            val currentValue = romanMap[s[i]] ?: 0
            sum += if (currentValue > prevValue) {
                -prevValue
            } else {
                prevValue
            }
            prevValue = currentValue
        }
        sum += prevValue

        return sum
    }

    private val romanMap = mapOf(
        'I' to 1,
        'V' to 5,
        'X' to 10,
        'L' to 50,
        'C' to 100,
        'D' to 500,
        'M' to 1000
    )
}
