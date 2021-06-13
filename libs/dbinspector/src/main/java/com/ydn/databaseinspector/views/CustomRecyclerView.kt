package com.ydn.databaseinspector.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.ydn.databaseinspector.utilities.dpsToPixels

class CustomRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val hSpec = MeasureSpec.makeMeasureSpec(dpsToPixels(resources,150f).toInt(), MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, hSpec)
    }
}