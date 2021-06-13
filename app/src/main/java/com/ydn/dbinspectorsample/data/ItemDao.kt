package com.ydn.dbinspectorsample.data

import androidx.room.*

@Dao
interface ItemDao {
    @Insert
    fun insert(vararg items: Item)

    @Update
    fun update(vararg items: Item)

    @Delete
    fun delete(item: Item)

    @Query("SELECT * FROM items")
    fun getItems(): List<Item>

    @Query("DELETE FROM items")
    fun deleteTable()

    @Query("UPDATE sqlite_sequence SET seq = 1 WHERE name = 'items'")
    fun clearPrimaryKey()
}