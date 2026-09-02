package com.asteam.ashpazyar.auto

// Activity ورودی Android Automotive آشپزیار؛ رابط مشترک را در حالت صفحه بزرگ اجرا می‌کند.

import MainView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivityAutomotive : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainView(isLargeScreen = true) }
    }
}
