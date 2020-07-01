# SplitEditTextView
Android类似支付宝密码输入框，美团外卖验证码输入框等等支持下划线，方框，连体框样式；可设置光标、光标颜色，边框大小、颜色、圆角等等；设置密码显示的样式；不能满足需求也可自行将library里面的源码下载下来进行修改
# 效果图
![image](https://github.com/Chen-keeplearn/SplitEditTextView/blob/other/screenshot/SplitEditTextView_Screenshot_01.jpg)
![image](https://github.com/Chen-keeplearn/SplitEditTextView/blob/other/screenshot/SplitEditTextView_Screenshot_02.jpg)
![image](https://github.com/Chen-keeplearn/SplitEditTextView/blob/other/screenshot/SplitEditTextView_Gif.gif)
# 如何使用
**第一步 依赖**
首先将SplitEditTextView引入到您的项目中，如下：
``` groovy
dependencies {
   ...
   implementation 'com.open.keeplearn:SplitEditTextView:1.2.0'  
}
```
**第二步 xml中使用**

默认直接弹出键盘
``` xml
<com.al.open.SplitEditTextView
    android:id="@+id/splitEdit1"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="20dp"
    android:inputType="number"
    android:textColor="@color/colorPrimary"
    android:textSize="30sp"
    app:borderColor="@color/colorPrimaryDark"
    app:borderSize="2dp"
    app:contentNumber="4"
    app:contentShowMode="password"
    app:cornerSize="10dp"
    app:cursorColor="@color/colorAccent"
    app:cursorWidth="6dp"
    app:inputBoxStyle="singleBox"
    app:spaceSize="20dp" />
```
若默认不弹出键盘，点击弹出键盘，需要按照以前的方式，在SliptEditTextView的parent布局，其它外层布局或者根布局添加两个属性：
``` xml
android:focusable="true"
android:focusableInTouchMode="true"
```
``` xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:focusable="true"
    android:focusableInTouchMode="true"
    android:gravity="center_horizontal"
    android:orientation="vertical"
    android:padding="16dp">
        
    <com.al.open.SplitEditTextView
        android:id="@+id/splitEdit2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        app:borderColor="@color/colorPrimary"
        app:borderSize="2dp"
        app:contentNumber="5"
        app:contentShowMode="text"
        app:cursorDuration="1000"
        app:inputBoxStyle="underline"
        app:spaceSize="20dp" />

 </LinearLayout>
```
