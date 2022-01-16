package com.example.pagingtest.api

import com.example.pagingtest.db.ContentEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CafeDocuments(
    val title: String = "",
    val contents: String = "",
    val url: String = "",
    @Json(name = "cafename") val cafeName: String = "",
    val thumbnail: String = "",
    val datetime: String = ""
)

fun List<CafeDocuments>.asDatabaseModel(): Array<ContentEntity> {
    return map {
        ContentEntity(
            thumbnail = it.thumbnail,
            label = "cafe",
            name = it.cafeName,
            title = it.title,
            contents = it.contents,
            dateTime = it.datetime,
            url = it.url,
            isClicked = false
        )
    }.toTypedArray()
}
