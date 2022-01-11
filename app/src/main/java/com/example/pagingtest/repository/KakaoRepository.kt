package com.example.pagingtest.repository

import com.example.pagingtest.api.KakaoService

class KakaoRepository(private val service: KakaoService) {



    companion object {
        const val NETWORK_PAGE_SIZE = 25
    }
}