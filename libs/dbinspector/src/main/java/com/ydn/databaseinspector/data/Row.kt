package com.ydn.databaseinspector.data

class Row(val id: Int) {
    var cells: MutableList<Cell> = mutableListOf()

    fun add(cell: Cell) = cells.add(cell)
}