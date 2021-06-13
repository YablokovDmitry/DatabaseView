package com.ydn.databaseinspector.data

import android.database.sqlite.SQLiteDatabase
import java.io.File

class Database(val file: File, val sqLiteDatabase: SQLiteDatabase, val tables: List<TableInfo>)