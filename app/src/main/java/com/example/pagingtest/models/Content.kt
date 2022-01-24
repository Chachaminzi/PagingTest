package com.example.pagingtest.models

import android.os.Parcelable
import android.util.Log
import com.example.pagingtest.api.BlogDocuments
import com.example.pagingtest.api.CafeDocuments
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class Content(
    val thumbnail: String,
    val label: String,
    val name: String,
    val title: String,
    val contents: String,
    val dateTime: String,
    val url: String,
    var isClicked: Boolean
) : Parcelable {
    constructor(cafe: CafeDocuments) : this(
        cafe.thumbnail,
        "cafe",
        cafe.cafeName,
        cafe.title,
        cafe.contents,
        cafe.datetime,
        cafe.url,
        false
    )

    constructor(blog: BlogDocuments) : this(
        blog.thumbnail,
        "blog",
        blog.blogName,
        blog.title,
        blog.contents,
        blog.datetime,
        blog.url,
        false
    )

    fun displayText(format: String): String {
        val resultParsing = LocalDate.parse(this.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return resultParsing.format(DateTimeFormatter.ofPattern(format)).toString()
    }

    fun getTimeStamp(): Long {
        val zonedDateTime = ZonedDateTime.parse(this.dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return Timestamp.from(zonedDateTime.toInstant()).time
    }
}