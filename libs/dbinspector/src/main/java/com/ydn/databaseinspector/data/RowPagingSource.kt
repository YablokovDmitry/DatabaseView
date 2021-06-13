package com.ydn.databaseinspector.data

import android.database.sqlite.SQLiteDatabase
import androidx.paging.PagingSource

class RowPagingSource(
    private val database: SQLiteDatabase,
    private val tableInfo: TableInfo,
) : PagingSource<Int, Row>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Row> {
        val page = params.key ?: 1
        return try {
            val rows: MutableList<Row> = mutableListOf()

            val cursor = database.rawQuery(
                "select * from " + tableInfo.name + " limit " + params.loadSize + " offset " + (page - 1) * params.loadSize,
                null
            )
            cursor.use { c ->
                if (c!!.moveToFirst()) {
                    do {
                        val row = Row(rows.size - 1)
                        for (info in tableInfo.rowInfos) {
                            var value = ""
                            val index = c.getColumnIndex(info.name)

                            when (info.type) {
                                "TEXT" -> value = c.getString(index)
                                "INTEGER" -> value = c.getInt(index).toString()
                            }
                            row.add(Cell(info.name, value))
                        }
                        rows.add(row)
                    } while (c.moveToNext())
                }
            }

            LoadResult.Page(
                data = rows,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (rows.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}