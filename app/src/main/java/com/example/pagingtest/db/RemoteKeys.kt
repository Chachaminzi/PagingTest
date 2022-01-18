package com.example.pagingtest.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val remoteId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
