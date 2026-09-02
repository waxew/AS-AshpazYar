// ورودی نسخه دسکتاپ آشپزیار؛ پنجره Compose مشترک را با نام محصول اجرا می‌کند.

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(title = "آشپزیار | AS Team", onCloseRequest = ::exitApplication) {
        MainView()
    }
}
