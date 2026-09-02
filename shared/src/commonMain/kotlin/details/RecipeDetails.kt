package details

// پوسته جزئیات دستور غذا؛ جهت RTL را روی نسخه کوچک و بزرگ یکسان اعمال می‌کند.

import RecipeDetailsSmall
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import model.Recipe
import sensor.SensorManager

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeDetails(
    recipe: Recipe,
    goBack: () -> Unit,
    sensorManager: SensorManager?,
    isLarge: Boolean,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransactionScope: SharedTransitionScope
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        if (isLarge) {
            RecipeDetailsLarge(
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransactionScope = sharedTransactionScope,
                recipe = recipe,
                goBack = goBack,
                sensorManager = sensorManager
            )
        } else {
            RecipeDetailsSmall(
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransactionScope = sharedTransactionScope,
                recipe = recipe,
                goBack = goBack,
                sensorManager = sensorManager
            )
        }
    }
}
