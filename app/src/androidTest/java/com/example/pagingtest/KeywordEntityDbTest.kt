package com.example.pagingtest

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.pagingtest.db.KeywordEntity
import com.example.pagingtest.db.KeywordDao
import com.example.pagingtest.db.SearchDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class KeywordEntityDbTest {
    private lateinit var keywordDao: KeywordDao
    private lateinit var db: SearchDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, SearchDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        keywordDao = db.keywordDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertKeyword() {
        val keywords = listOf(
            KeywordEntity(recordName = "딸기"),
            KeywordEntity(recordName = "바나나"),
            KeywordEntity(recordName = "초코"),
            KeywordEntity(recordName = "민트"),
            KeywordEntity(recordName = "얼그레이")
        )

        runBlocking {
            for (key in keywords) {
                keywordDao.insert(key)
            }
        }

        val lists = keywordDao.testAllKeyword()
        assertEquals(5, lists.size)
    }
}