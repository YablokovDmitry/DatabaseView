package com.ydn.dbinspectorsample.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item (val name: String, val param: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}