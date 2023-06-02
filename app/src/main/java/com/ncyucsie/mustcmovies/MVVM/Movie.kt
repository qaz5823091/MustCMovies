package com.ncyucsie.mustcmovies.MVVM

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey(autoGenerate = true)
    var id : Int?=null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "rating") val rating: Float,
    @ColumnInfo(name = "comment") val comment: String,
    var order: Int = 0
) {
    override fun toString(): String {
        return "Title: $title, rating: $rating, comment: $comment order: $order"
    }
}