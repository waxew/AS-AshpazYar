package recipeslist

// صفحه اصلی فهرست دستورها؛ جست‌وجو، دسته‌بندی، علاقه‌مندی و RTL را برای همه پلتفرم‌ها فراهم می‌کند.

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as rowItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import model.Recipe
import sugar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipesListScreen(
    items: List<Recipe>,
    favorites: Set<Int>,
    showOnlyFavorites: Boolean,
    onToggleFavorite: (recipe: Recipe) -> Unit,
    onMenuClick: () -> Unit,
    onClick: (recipe: Recipe) -> Unit,
    isLarge: Boolean,
    sharedTransactionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("همه") }
    val categories = remember(items) { listOf("همه") + items.map { it.category }.distinct() }
    val visibleItems = items.filter { recipe ->
        val favoriteMatches = !showOnlyFavorites || recipe.id in favorites
        val categoryMatches = selectedCategory == "همه" || recipe.category == selectedCategory
        val normalized = query.trim()
        val searchMatches = normalized.isBlank() ||
            recipe.title.contains(normalized, ignoreCase = true) ||
            recipe.description.contains(normalized, ignoreCase = true) ||
            recipe.ingredients.any { it.contains(normalized, ignoreCase = true) } ||
            recipe.tags.any { it.contains(normalized, ignoreCase = true) }
        favoriteMatches && categoryMatches && searchMatches
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(modifier = Modifier.fillMaxSize().background(sugar).windowInsetsPadding(WindowInsets.systemBars)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // در RTL اولین عضو Row در سمت راست قرار می‌گیرد، بنابراین منو قبل از عنوان آمده است.
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "باز کردن منو")
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        text = if (showOnlyFavorites) "علاقه‌مندی‌ها" else "آشپزیار",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (showOnlyFavorites) "دستورهای ذخیره‌شده شما" else "امروز چی درست کنیم؟",
                        style = MaterialTheme.typography.caption
                    )
                }
            }

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                placeholder = { Text("جست‌وجوی غذا، ماده اولیه یا برچسب") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(Modifier.size(8.dp)) }
                rowItems(categories) { category ->
                    if (category == selectedCategory) {
                        Button(onClick = { selectedCategory = category }) { Text(category) }
                    } else {
                        OutlinedButton(onClick = { selectedCategory = category }) { Text(category) }
                    }
                }
                item { Spacer(Modifier.size(8.dp)) }
            }

            if (visibleItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (showOnlyFavorites && favorites.isEmpty())
                            "هنوز غذایی به علاقه‌مندی‌ها اضافه نکرده‌اید."
                        else "نتیجه‌ای برای این جست‌وجو پیدا نشد."
                    )
                }
            } else {
                val listState = rememberLazyGridState()
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Fixed(if (isLarge) 3 else 1),
                    modifier = Modifier.fillMaxSize()
                ) {
                    gridItems(visibleItems, key = { it.id }) { recipe ->
                        RecipeListItemWrapper(
                            scrollDirection = listState.isScrollingUp(),
                            child = {
                                RecipeListItem(
                                    recipe = recipe,
                                    isFavorite = recipe.id in favorites,
                                    onToggleFavorite = onToggleFavorite,
                                    onClick = onClick,
                                    sharedTransitionScope = sharedTransactionScope,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

/** تشخیص جهت اسکرول برای حفظ انیمیشن کارت‌های موجود. */
@Composable
private fun LazyGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
