package com.example.pagingtest

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun convertStringToDateString(param: String): String {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val reFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    val resultParsing = LocalDate.parse(param, formatter)
    return resultParsing.format(reFormatter).toString()
}