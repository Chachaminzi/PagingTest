package com.example.pagingtest

import android.util.Log
import org.bouncycastle.asn1.cms.TimeStampAndCRL
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.sql.Timestamp
import java.time.*
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
        val string1 = "2017-05-06T00:36:45+04:00"
        val string2 = "2017-05-06T00:36:45+03:00"

//        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
//        val reFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
//        val resultParsing = LocalDate.parse(string, formatter)
//        val result = resultParsing.format(reFormatter)
//
//        assertEquals(resultParsing.toString(), "2017-05-06")
//        assertEquals(result.toString(), "2017년 05월 06일")

        val zonedDateTime1 = ZonedDateTime.parse(string1, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val zonedDateTime2 = ZonedDateTime.parse(string2, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        val ztts1 = Timestamp.from(zonedDateTime1.toInstant())
        val ztts2 = Timestamp.from(zonedDateTime2.toInstant())

        assertEquals(ztts1.time, ztts2.time)
    }
}