package com.ydn.databaseinspector.adapters

import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ydn.databaseinspector.R
import com.ydn.databaseinspector.adapters.TablePagingAdapter.ItemViewHolder
import com.ydn.databaseinspector.data.Row


class TablePagingAdapter : PagingDataAdapter<Row, ItemViewHolder>(RowDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item, parent,
            false
        )

        val size = getItem(0)!!.cells.size - 1

        with(LinearLayout(parent.context)) {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            background = parent.context.getDrawable(R.drawable.ic_table_item)

            for (i in 0..size) {
                val tv = TextView(parent.context).apply {
                    width = 300
                    isSingleLine = true
                    ellipsize = TextUtils.TruncateAt.END
                    setLines(1)
                    setTextColor(Color.BLACK)
                    tag = i + 1
                }
                addView(tv)

                val lp = RecyclerView.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT)
                val del = View(parent.context).apply {
                    this.layoutParams = lp
                    setBackgroundColor(Color.BLACK)
                }
                addView(del)
            }
            (view as LinearLayout).addView(this)
        }
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        for (child in holder.view.children) {
            if (child is LinearLayout) {
                var index = 0
                for (ch in child.children) {
                    if (ch is TextView) {
                        val row = getItem(position)
                        val value = row?.cells?.get(index)?.value
                        ch.text = "   $value   "
                        index++
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 0
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: ViewGroup = itemView as ViewGroup
    }
}

private class RowDiffCallback : DiffUtil.ItemCallback<Row>() {
    override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean {
        return oldItem.cells == newItem.cells
    }
}

