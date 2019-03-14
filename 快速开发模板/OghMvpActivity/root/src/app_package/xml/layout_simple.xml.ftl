<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
	tools:context="${packageName}.view.<#if selectViewType == "activity">activity<#else>fragment</#if>.${activityClass}<#if selectViewType == "activity">Activity<#else>Fragment</#if>">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="布局示例" />
</LinearLayout>