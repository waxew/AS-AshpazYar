// ورودی Android آشپزیار؛ اتصال UI مشترک به SharedPreferences، Share Sheet و صفحه نسخه‌های GitHub.

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import sensor.SensorManagerImpl

private const val PREFS_NAME = "ashpazyar_user_prefs"
private const val KEY_FAVORITES = "favorite_recipe_ids"
private const val KEY_QUICK_HINTS = "show_quick_hints"

/**
 * Stateهای قابل حفظ کاربر از SharedPreferences خوانده می‌شوند؛ چون Package ثابت است،
 * این داده‌ها پس از نصب نسخه‌های جدید برنامه نیز باقی می‌مانند.
 */
@Composable
fun MainView(isLargeScreen: Boolean = false) {
    val context = LocalContext.current
    val sensorManager = remember { SensorManagerImpl(context) }
    val preferences = remember {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    val initialFavorites = remember {
        preferences.getStringSet(KEY_FAVORITES, emptySet())
            .orEmpty()
            .mapNotNull { it.toIntOrNull() }
            .toSet()
    }
    val initialShowQuickHints = remember {
        preferences.getBoolean(KEY_QUICK_HINTS, true)
    }

    DisposableEffect(Unit) {
        onDispose {
            sensorManager.cancel()
        }
    }

    App(
        sensorManager = sensorManager,
        isLarge = isLargeScreen,
        initialFavorites = initialFavorites,
        onFavoritesChanged = { favorites ->
            preferences.edit()
                .putStringSet(KEY_FAVORITES, favorites.map { it.toString() }.toSet())
                .apply()
        },
        initialShowQuickHints = initialShowQuickHints,
        onShowQuickHintsChanged = { enabled ->
            preferences.edit().putBoolean(KEY_QUICK_HINTS, enabled).apply()
        },
        onShareApp = {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "آشپزیار | دستیار آشپزی AS Team\nhttps://github.com/waxew/AS-AshpazYar"
                )
            }
            context.startActivity(Intent.createChooser(shareIntent, "اشتراک‌گذاری آشپزیار"))
        },
        onCheckForUpdates = {
            val releasesPage = Uri.parse("https://github.com/waxew/AS-AshpazYar/releases")
            context.startActivity(Intent(Intent.ACTION_VIEW, releasesPage))
        }
    )
}
