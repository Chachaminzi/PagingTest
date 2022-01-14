package com.example.pagingtest

import java.text.SimpleDateFormat

fun convertStringToDateString(param: String): String {
    val splitString = param.split("T")
    val date = SimpleDateFormat("yyyy-MM-dd").parse(splitString[0])
    return SimpleDateFormat("yyyy년 MM월 dd일").format(date)
}