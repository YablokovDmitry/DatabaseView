package com.ydn.dbinspectorsample.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun load(userId: String): Flow<User>

    @Query("DELETE FROM users WHERE ID=(SELECT MAX(id) FROM users)")
    fun removeLast()

    @Query("DELETE FROM users")
    fun deleteTable()

    @Query("UPDATE sqlite_sequence SET seq = 1 WHERE name = 'users'")
    fun clearPrimaryKey()
}