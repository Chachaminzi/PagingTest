package com.example.pagingtest.viewmodels

import androidx.paging.PagingData
import com.example.pagingtest.api.CafeSearchResponse
import com.example.pagingtest.api.KakaoService
import com.example.pagingtest.db.SearchDatabase
import com.example.pagingtest.models.Content
import com.example.pagingtest.repository.KakaoRepository
import io.mockk.MockK
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ContentListViewModelTest {

    @Mock
    private lateinit var repository: KakaoRepository

    @Mock
    private lateinit var service: KakaoService

    @Mock
    private lateinit var database: SearchDatabase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        service = mockk()
        database = mockk()
        repository = KakaoRepository(database, service)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun searchCafe() {
        // 생성자 확인
        val sample = mockk<Content> {
            every { title } returns "sample"
        }

        val result = sample.title
        Assert.assertTrue(result == "sample")
    }

    @Test
    fun isCalledCafe() = runTest {
        // 반환 타입 확인
        var result = mockk<CafeSearchResponse>()
        coEvery {
            repository.testCafe("우유").collect {
                result = it
            }
        }

        Assert.assertTrue(result is CafeSearchResponse)
    }

    @Test
    fun isCalledOnce() = runTest {
//        `when`(repository.getCafeResultStream("우유")).thenReturn(
//            flowOf(
//                PagingData.from(
//                    listOf(
//                        Content(
//                            "https://search1.kakaocdn.net/thumb/P100x100/?fname=http%3A%2F%2Fsearch1.kakaocdn.net%2Fargon%2F130x130_85_c%2FFJtvuJLusO",
//                            "cafe",
//                            "쭉빵카페",
//                            "연예인 무대의상 정보 가져왔어! (feat. 레드벨벳, <b>아이유</b>)",
//                            "무대의상들 정보 궁금해하는 게녀들이 있길래 가져왔어! 이번에는 레드벨벳이랑 <b>아이유</b> 의상만 가져왔는데 기회가 되면 다음에 다른 연예인들 의상도 가져올게~ 링크가...",
//                            "",
//                            "http://cafe.daum.net/ok1221/9fQk/39709"
//                        )
//                    )
//                )
//            )
//        )

        // 몇 번 호출되었는지
        val repo = mock(KakaoRepository::class.java)

        repo.getCafeResultStream("우유")

        verify(repo, atLeastOnce()).getCafeResultStream("우유")

        repo.getCafeResultStream("우유")
        repo.getCafeResultStream("우유")

        verify(repo, atLeast(3)).getCafeResultStream("우유")
    }
}