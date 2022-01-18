package com.example.pagingtest

import com.example.pagingtest.api.CafeSearchResponse
import com.example.pagingtest.api.Network
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun date_format() {
        val string = "2017-05-06T00:36:45+09:00"
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val reFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        val resultParsing = LocalDate.parse(string, formatter)
        val result = resultParsing.format(reFormatter)

        assertEquals(resultParsing.toString(), "2017-05-06")
        assertEquals(result.toString(), "2017년 05월 06일")
    }
}