package com.example.pagingtest.models

import com.example.pagingtest.api.BlogDocuments
import com.example.pagingtest.api.CafeDocuments
import com.example.pagingtest.db.ContentEntity

data class Content(
    val thumbnail: String,
    val label: String,
    val name: String,
    val title: String,
    val contents: String,
    val dateTime: String,
    val url: String,
    var isClicked: Boolean
) {
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

    constructor(entity: ContentEntity) : this(
        entity.thumbnail,
        entity.label,
        entity.name,
        entity.title,
        entity.contents,
        entity.dateTime,
        entity.url,
        entity.isClicked
    )
}