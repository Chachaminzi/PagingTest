package com.example.pagingtest

import com.example.pagingtest.api.CafeSearchResponse
import com.example.pagingtest.api.Network
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

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
    fun apiTest() {
        val boo = coEvery {
            var ret = Network.testApi.searchCafe(query = "커피", page = 1, size = 20)
            return@coEvery ret.documents.isNotEmpty()
        }

        assertEquals(true, boo)
    }
}