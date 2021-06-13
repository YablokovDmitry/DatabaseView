package com.ydn.databaseinspector.data

data class RowInfo(
    val cid: Long = 0,
    val name: String= "",
    val type: String= "",
    val notnull: Boolean = false,
    val dflt_value: Long = 0,
    val pk: Long = 0
)