package com.ydn.databaseinspector.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.paging.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class DatabaseInspectorRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun getDatabases(): List<Database> {
        val databases = mutableListOf<Database>()

        withContext(Dispatchers.IO) {
            val databasesDir = File(context.applicationInfo.dataDir.toString() + "/databases")
            databasesDir.listFiles()?.forEach {
                try {
                    if (!it.absolutePath.contains("-wal") and !it.absolutePath.contains("-shm")) {
                        val tableInfos = mutableListOf<TableInfo>()
                        val database = SQLiteDatabase.openDatabase(
                            it.absolutePath,
                            null,
                            SQLiteDatabase.OPEN_READONLY
                        )

                        val cursor = database.rawQuery(
                            "select name from sqlite_master where type = 'table'",
                            null
                        )
                        cursor.use { c ->
                            while (c.moveToNext()) {
                                do {
                                    val tableName = c.getString(cursor.getColumnIndex("name"))
                                    tableInfos.add(
                                        TableInfo(
                                            tableName,
                                            getRowInfo(database, tableName)
                                        )
                                    )
                                } while (c.moveToNext())
                            }
                        }

                        databases.add(Database(it, database, tableInfos))
                    }
                } catch (exception: Exception) {
                   Log.ERROR
                }
            }
        }
        return databases
    }

    private fun getRowInfo(db: SQLiteDatabase, tableName: String): List<RowInfo> {
        val rowInfos = mutableListOf<RowInfo>()

        val cursor = db.rawQuery("PRAGMA table_info($tableName)", null)
        cursor.use { c ->
            while (c.moveToNext()) {
                do {
                    val name = c.getString( c.getColumnIndex("name") )
                    val cid = c.getLong( c.getColumnIndex("cid"))
                    val type = c.getString( c.getColumnIndex("type") )
                    val notnull = c.getInt( c.getColumnIndex("notnull") ) > 0
                    val dfltValue = c.getLong( c.getColumnIndex("dflt_value") )
                    val pk = c.getLong( c.getColumnIndex("pk") )

                    rowInfos.add(RowInfo(cid, name, type, notnull, dfltValue, pk))

                } while (cursor.moveToNext())
            }
        }
        return rowInfos
    }

    fun getTableDataStream(database: Database, table: TableInfo): Flow<PagingData<Row>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = TABLE_PAGE_SIZE),
            pagingSourceFactory = { RowPagingSource(database.sqLiteDatabase, table) }
        ).flow
    }

    companion object {
        private const val TABLE_PAGE_SIZE = 25
    }
}