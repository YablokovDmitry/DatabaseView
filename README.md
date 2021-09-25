# Android Database Inspector View 

A floating view displays a list of the databases in your app and the tables that each database contains.

<img src=https://user-images.githubusercontent.com/3678050/121823201-51939600-ccac-11eb-97ca-0da476203e13.gif width="265" height="550">



## **Usage**

AndroidManifest.xml

    <uses-permission android:name="android.permission.ACTION_MANAGE_OVERLAY_PERMISSION" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

onCreate()

    // width = 200dp, height = 350dp
    floatingWindow = DatabaseInspectorFloatingWindow(this, 200f, 350f, lifecycle)

Ask for screen overlay permission

    fun startManageDrawOverlaysPermission() {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${applicationContext.packageName}")
            ).let {
                startActivityForResult(it, REQUEST_CODE_DRAW_OVERLAY_PERMISSION)
            }
        }
    }

Show window
 
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

        
 ### **Developed By**
  - Dmitry Yablokov - [dnyablokov@gmail.com](mailto:dnyablokov@gmail.com)


  ### **License**
```      

Copyright 2021 Dmitry Yablokov.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```      

