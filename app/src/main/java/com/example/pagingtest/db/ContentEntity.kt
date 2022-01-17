package com.example.pagingtest.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contents")
data class ContentEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var thumbnail: String,
    var label: String,
    var name: String,
    var title: String,
    var contents: String,
    var dateTime: String,
    var url: String,
    var isClicked: Boolean
)