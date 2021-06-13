package com.ydn.databaseinspector


import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import android.widget.Button
import androidx.lifecycle.Lifecycle
import com.ydn.databaseinspector.extensions.canDrawOverlays
import com.ydn.databaseinspector.utilities.dpsToPixels
import com.ydn.databaseinspector.utilities.pixelsToDip
import java.lang.Math.abs

class DatabaseInspectorFloatingWindow constructor(private val context: Context,
                                                  private var dpWidth: Float,
                                                  private var dpHeight: Float,
                                                  private val lifecycle: Lifecycle) {

    private var windowManager: WindowManager? = null
        get() {
            if (field == null) field = (context.getSystemService(WINDOW_SERVICE) as WindowManager)
            return field
        }

    private var floatView: View =
        LayoutInflater.from(context).inflate(R.layout.layout_floating_window, null)

    private var titleView: View
    private var databaseInspectorView: DatabaseInspectorView

    private lateinit var windowLayoutParams: WindowManager.LayoutParams

    private var lastX: Int = 0
    private var lastY: Int = 0
    private var firstX: Int = 0
    private var firstY: Int = 0

    var isShowing = false
    private var touchConsumedByMove = false

    private var zoom = 1f

    private val onTouchListener = View.OnTouchListener { view, event ->
        val totalDeltaX = lastX - firstX
        val totalDeltaY = lastY - firstY

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
                firstX = lastX
                firstY = lastY
            }
            MotionEvent.ACTION_UP -> {
                view.performClick()
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.rawX.toInt() - lastX
                val deltaY = event.rawY.toInt() - lastY
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
                if (abs(totalDeltaX) >= 5 || abs(totalDeltaY) >= 5) {
                    if (event.pointerCount == 1) {
                        windowLayoutParams.x += deltaX
                        windowLayoutParams.y += deltaY
                        touchConsumedByMove = true
                        windowManager?.apply {
                            updateViewLayout(floatView, windowLayoutParams)
                        }
                    } else {
                        touchConsumedByMove = false
                    }
                } else {
                    touchConsumedByMove = false
                }
            }
            else -> {
            }
        }
        touchConsumedByMove
    }

    init {
        with(floatView) {
            titleView = findViewById(R.id.title)

            setOnTouchListener(onTouchListener)

            val zoomInBtn = findViewById<Button>(R.id.zoom_in)
            zoomInBtn.setOnClickListener {
                if (pixelsToDip(context.resources, width.toFloat()) < 300) {
                    zoom *= 1.5f
                    resize()
                    windowManager?.apply { updateViewLayout(floatView, windowLayoutParams) }
                }

            }

            val zoomOutBtn = findViewById<Button>(R.id.zoom_out)
            zoomOutBtn.setOnClickListener {
                if (pixelsToDip(context.resources, width.toFloat()) > 150) {
                    zoom /= 1.5f

                    resize()
                    windowManager?.apply { updateViewLayout(floatView, windowLayoutParams) }
                }
            }

        }
        windowLayoutParams = WindowManager.LayoutParams().apply {
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            @Suppress("DEPRECATION")
            type = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else -> WindowManager.LayoutParams.TYPE_TOAST
            }

            gravity = Gravity.CENTER

            width = (dpsToPixels(context.resources, dpWidth).toInt() * zoom).toInt()
            height = (dpsToPixels(context.resources, dpHeight).toInt() * zoom).toInt()
        }

        titleView.layoutParams.width = windowLayoutParams.width

        with (floatView) {
            databaseInspectorView = findViewById(R.id.database_inspector)
            with(databaseInspectorView) {
                layoutParams.width = windowLayoutParams.width - 10
                x = 5f

                registerLifecycleOwner(lifecycle)
                refresh()

                val refreshBtn = floatView.findViewById<Button>(R.id.refresh)
                refreshBtn.setOnClickListener { refresh() }
            }
        }
    }

    private fun resize() {
        with (windowLayoutParams) {
            width = (dpsToPixels(context.resources, dpWidth).toInt() * zoom).toInt()
            height = (dpsToPixels(context.resources, dpHeight).toInt() * zoom).toInt()
        }

        with (databaseInspectorView) {
            x = 5f
            y = dpsToPixels(context.resources, 26f)
            layoutParams.width = windowLayoutParams.width - dpsToPixels(context.resources, 5f).toInt()
            layoutParams.height = ((windowLayoutParams.height - databaseInspectorView.y * 1.2f - 0)* 1).toInt()

            floatView.setBackgroundResource(R.drawable.ic_floating_wnd_background)
            titleView.layoutParams.width = windowLayoutParams.width
        }
    }

    fun show() {
        if (context.canDrawOverlays) {
            dismiss()
            isShowing = true
            windowManager?.addView(floatView, windowLayoutParams)
        }
    }

    fun dismiss() {
        if (isShowing) {
            windowManager?.removeView(floatView)
            isShowing = false
        }
    }
}