# Android Database Inspector View 

A floating view displays a list of the databases in your app and the tables that each database contains.

<img src=https://user-images.githubusercontent.com/3678050/121823201-51939600-ccac-11eb-97ca-0da476203e13.gif width="265" height="550">



## **Usage**

Add it in your root build.gradle at the end of repositories:

     repositories {
	    ...
        maven { url "https://jitpack.io" }
     }	
Add the dependency

     dependencies {
         ...
         implementation 'com.github.YablokovDmitry:DatabaseView:1.2'
     }
#### onCreate()

     // width = 200dp, height = 350dp
     val floatingWindow = DatabaseInspectorFloatingWindow(this, 200f, 350f, lifecycle)
        
     // Show
     floatingWindow.show()
        
     // Hide
     floatingWindow.dismiss()
        
 ### **Developed By**
  - Dmitry Yablokov - [dnyablokov@gmail.com](mailto:dnyablokov@gmail.com)


  ### **License**
```      

MIT License

Copyright (c) 2021 Dmitry Yablokov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```      

