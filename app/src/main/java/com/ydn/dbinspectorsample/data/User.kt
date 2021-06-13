package com.ydn.dbinspectorsample.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (val name: String, val lastName: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}