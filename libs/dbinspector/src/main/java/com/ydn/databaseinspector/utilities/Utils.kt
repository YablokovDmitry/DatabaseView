package com.ydn.databaseinspector.utilities

import android.content.res.Resources
import android.util.TypedValue

fun dpsToPixels(r: Resources, dip: Float) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dip,
    r.getDisplayMetrics()
)

fun pixelsToDip(r: Resources, px: Float) = px / TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    1f,
    r.getDisplayMetrics()
)
