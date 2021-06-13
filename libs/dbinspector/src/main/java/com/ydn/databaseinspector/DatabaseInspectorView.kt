package com.ydn.databaseinspector

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ydn.databaseinspector.adapters.TablePagingAdapter
import com.ydn.databaseinspector.data.Database
import com.ydn.databaseinspector.data.TableInfo
import com.ydn.databaseinspector.viewmodel.DatabaseInspectorViewModel
import com.ydn.databaseinspector.views.CustomRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DatabaseInspectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private  lateinit var coroutineScope: LifecycleCoroutineScope

    private var mainLayout: LinearLayout
    private var swipeRefreshLayout: SwipeRefreshLayout
    private var viewModel: DatabaseInspectorViewModel

    init {
        val rootView = (context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.view_database_inspector, this, true)

        mainLayout = rootView.findViewById(R.id.scroll_layout)
        viewModel = ViewModelProvider(context as ViewModelStoreOwner).get(DatabaseInspectorViewModel::class.java)

        swipeRefreshLayout = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
            .also{ it.setOnRefreshListener { refresh() } }

        with(viewModel) {
            databases.observe(context as LifecycleOwner, Observer {
                drawDatabases()
            } )
        }
    }

    fun registerLifecycleOwner(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
        coroutineScope = lifecycle.coroutineScope
    }

    fun refresh() {
        //swipeRefreshLayout.post { /*swipeRefreshLayout.isRefreshing = true*/ }
        coroutineScope.launch {
            viewModel.loadDatabases()
            drawDatabases()
        }
    }

    private fun drawTableName(table: TableInfo) {
        val title = TextView(context).apply {
            text = "table: \"${table.name}\""
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.BLACK)
        }
        mainLayout.addView(title)
    }

    private fun drawTableInfo(table: TableInfo) {
        LinearLayout(context).apply {
            for (info in table.rowInfos) {
                val tv = TextView(context).apply {
                    text = "   ${info.name}"
                    width = 300
                    isSingleLine = true
                    ellipsize = TextUtils.TruncateAt.END
                    setLines(1)
                    setBackgroundColor(Color.parseColor("#ABB2B9"))
                    setTextColor(Color.WHITE)
                    setTypeface(Typeface.DEFAULT_BOLD)
                }
                addView(tv)
            }
            mainLayout.addView(this)
        }
    }

    private fun drawDatabases() {
        if(viewModel.databases.value == null) {
            return
        }
        mainLayout.removeAllViews()

        for (db in viewModel.databases.value!!) {
            with(TextView(context)) {
                text = "DB: \"" + db.file.name + "\""
                textSize = 18f
                //gravity=Gravity.CENTER@android:color/holo_blue_light
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.parseColor("#0C9FDA"))
                mainLayout.addView(this)
            }
            drawTables(db)
        }
    }

    private fun drawTables(database: Database) {
        for (table in database.tables) {
            val tableAdapter = TablePagingAdapter()

            // Submit paged data to adapter
            coroutineScope.launch(Dispatchers.IO) {
                viewModel.loadTableData(database, table).collectLatest { pagingData ->

                    // Stop refreshing when last table loaded
                    if (table === viewModel.databases.value?.last()?.tables?.last()) {
                        swipeRefreshLayout.isRefreshing = false
                    }
                    tableAdapter.submitData(pagingData)
                }
            }

            drawTableName(table)
            drawTableInfo(table)

            val recyclerView = CustomRecyclerView(context).apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                layoutParams = RecyclerView.LayoutParams(300 * table.rowInfos.size, WRAP_CONTENT).also { it.setMargins(0, 0, 0, 50) }
                adapter = tableAdapter
            }
            mainLayout.addView(recyclerView)

        }
    }
}