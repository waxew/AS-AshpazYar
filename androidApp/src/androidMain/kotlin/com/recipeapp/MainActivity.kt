package com.asteam.ashpazyar

// Activity ورودی Android آشپزیار؛ هسته Compose مشترک را اجرا می‌کند و Edge-to-Edge را فعال نگه می‌دارد.

import MainView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { MainView() }
    }
}
