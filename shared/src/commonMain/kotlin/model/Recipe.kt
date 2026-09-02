package model

// مدل اصلی دستور غذا در آشپزیار؛ اطلاعات نمایشی و متادیتای لازم برای جست‌وجو و فیلتر را نگه می‌دارد.

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * داده‌ی یک دستور غذا.
 * category برای فیلتر، prep/cook برای زمان‌بندی، servings برای تعداد نفرات و difficulty برای نمایش سطح سختی استفاده می‌شوند.
 */
data class Recipe @OptIn(ExperimentalResourceApi::class) constructor(
    val id: Int,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val image: DrawableResource,
    val bgImage: DrawableResource? = null,
    val bgImageLarge: DrawableResource? = null,
    val bgColor: Color,
    val category: String = "سایر",
    val prepMinutes: Int = 10,
    val cookMinutes: Int = 20,
    val servings: Int = 4,
    val difficulty: String = "متوسط",
    val tags: List<String> = emptyList()
) {
    val totalMinutes: Int
        get() = prepMinutes + cookMinutes
}
