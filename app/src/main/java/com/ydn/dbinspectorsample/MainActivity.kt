package com.ydn.dbinspectorsample

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ydn.dbinspectorsample.data.AppDatabase
import com.ydn.dbinspectorsample.data.Item
import com.ydn.dbinspectorsample.data.User
import com.ydn.dbinspectorsample.databinding.ActivityMainBinding
import com.ydn.databaseinspector.DatabaseInspectorFloatingWindow
import com.ydn.databaseinspector.extensions.canDrawOverlays
import com.ydn.databaseinspector.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var floatingWindow: DatabaseInspectorFloatingWindow
    private lateinit var binding: ActivityMainBinding
    private var userCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#232f34")))
        floatingWindow = DatabaseInspectorFloatingWindow(this, 200f, 350f, lifecycle)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        with(binding) {
            addUserBtn.setOnClickListener {
                userCount++
                AppDatabase.getInstance(applicationContext).userDao()
                    .insert(User(name = "NewUser $userCount", lastName = "NewName $userCount"))
            }

            removeUserBtn.setOnClickListener {
                AppDatabase.getInstance(applicationContext).userDao().removeLast()
            }

            showDatabaseBtn.setOnClickListener {
                if (canDrawOverlays) {
                    with(floatingWindow) {
                        if (isShowing) {
                            dismiss()
                            showDatabaseBtn.text = "Show Database"
                        } else {
                            show()
                            showDatabaseBtn.text = "Hide Database"
                        }
                    }
                } else {
                    startManageDrawOverlaysPermission()
                }
            }
        }

        init()
    }

    private fun init() {
        val userDao = AppDatabase.getInstance(applicationContext).userDao()
        val itemDao = AppDatabase.getInstance(applicationContext).getItemDao()

        userDao.deleteTable()
        itemDao.deleteTable()

        userDao.clearPrimaryKey()
        itemDao.clearPrimaryKey()

        for (i in 0..30) {
            userDao.insert(User("User $i", "Name $i"))
            itemDao.insert(Item("Item $i", "Param $i"))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_DRAW_OVERLAY_PERMISSION -> {
                if (canDrawOverlays) {
                    floatingWindow.show()
                } else {
                    showToast("Permission is not granted!")
                }
            }
        }
    }

    private fun startManageDrawOverlaysPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${applicationContext.packageName}")
            ).let {
                startActivityForResult(it, REQUEST_CODE_DRAW_OVERLAY_PERMISSION)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_DRAW_OVERLAY_PERMISSION = 5
    }
}

