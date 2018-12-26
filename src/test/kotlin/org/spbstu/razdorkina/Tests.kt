package org.spbstu.razdorkina

import org.junit.Test
import kotlin.test.assertEquals
import GameRealization
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Tests {
    private val check = GameRealization(true)

    @Test
    fun checkFixDate() { //Делаем дату валидной для всех методов
        val date1 = "110"
        val date2 = "22.01"
        val date3 = "2 2"
        val date4 = "22 15"
        val date5 = "101"
        assertEquals("01.10", check.fixDate(date1))
        assertEquals("22.01", check.fixDate(date2))
        assertEquals("02.02", check.fixDate(date3))
        assertEquals("Incorrect date", check.fixDate(date4))
        assertEquals("Incorrect date", check.fixDate(date5))
    }

    @Test
    fun checkSolve() { //Проверяем корректность ходов бота
        val preDate1 = "31.10"
        val preDate2 = "26.12"
        val preDate3 = "28.02"
        val preDate4 = "31.08"
        assertEquals("31.12", check.solve(preDate1))
        assertEquals("27.12", check.solve(preDate2))
        assertEquals("28.04", check.solve(preDate3))
        assertEquals("31.10", check.solve(preDate4))
    }

    @Test
    fun checkIsLegalDate() { //Проверка существования даты
        val date1 = "28.02"
        val date2 = "29.02"
        val date3 = "31.06"
        val date4 = "31.07"
        assertTrue(check.isLegalDate(date1))
        assertFalse(check.isLegalDate(date2))
        assertFalse(check.isLegalDate(date3))
        assertTrue(check.isLegalDate(date4))
    }

    @Test
    fun checkFunCheckRules() { //Провека соблюдения правил
        val preDate1 = "15.12"
        val preDate2 = "03.07"
        val preDate3 = "07.09"
        assertTrue(check.checkRules(preDate1, "17.12"))
        assertFalse(check.checkRules(preDate2, "03.10"))
        assertFalse(check.checkRules(preDate3, "08.10"))
    }

}
