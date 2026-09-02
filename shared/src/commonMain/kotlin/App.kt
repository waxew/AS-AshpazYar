// هسته رابط کاربری آشپزیار؛ ناوبری، Drawer استاندارد AS Team و Stateهای اصلی برنامه را مدیریت می‌کند.

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import details.RecipeDetails
import kotlinx.coroutines.launch
import model.recipesList
import recipeslist.RecipesListScreen
import sensor.SensorManager

/** صفحه‌های سطح اول برنامه که از Drawer قابل دسترسی هستند. */
private enum class AppPage {
    Settings, Share, Home, Favorites, About, Contact
}

/**
 * ورودی اصلی رابط آشپزیار.
 * callbackهای persistence از commonMain تزریق می‌شوند تا Android داده کاربر را به‌صورت پایدار نگه دارد.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App(
    sensorManager: SensorManager?,
    isLarge: Boolean = false,
    initialFavorites: Set<Int> = emptySet(),
    onFavoritesChanged: (Set<Int>) -> Unit = {},
    initialShowQuickHints: Boolean = true,
    onShowQuickHintsChanged: (Boolean) -> Unit = {},
    onShareApp: () -> Unit = {},
    onCheckForUpdates: () -> Unit = {}
) {
    val fontFamily = getFontFamily()
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val favorites = remember(initialFavorites) {
        mutableStateListOf<Int>().apply { addAll(initialFavorites) }
    }
    var currentPage by remember { mutableStateOf(AppPage.Home) }
    var currentRecipe by remember { mutableStateOf(recipesList.first()) }
    var showQuickHints by remember(initialShowQuickHints) { mutableStateOf(initialShowQuickHints) }

    MaterialTheme(typography = getTypography(fontFamily)) {
        // RTL در سطح کل برنامه اعمال می‌شود؛ Drawer نیز از سمت راست باز می‌شود.
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            SharedTransitionLayout {
                val sharedTransitionScope = this
                NavHost(
                    navController = navController,
                    startDestination = RecipeAppScreen.List.name,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(route = RecipeAppScreen.List.name) {
                        ModalDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                AshpazYarDrawer(
                                    currentPage = currentPage,
                                    onSelect = { page ->
                                        currentPage = page
                                        scope.launch { drawerState.close() }
                                    }
                                )
                            }
                        ) {
                            when (currentPage) {
                                AppPage.Home, AppPage.Favorites -> RecipesListScreen(
                                    animatedVisibilityScope = this@composable,
                                    sharedTransactionScope = sharedTransitionScope,
                                    isLarge = isLarge,
                                    items = recipesList,
                                    favorites = favorites.toSet(),
                                    showOnlyFavorites = currentPage == AppPage.Favorites,
                                    onToggleFavorite = { recipe ->
                                        if (recipe.id in favorites) favorites.remove(recipe.id)
                                        else favorites.add(recipe.id)
                                        onFavoritesChanged(favorites.toSet())
                                    },
                                    onMenuClick = { scope.launch { drawerState.open() } },
                                    onClick = { recipe ->
                                        currentRecipe = recipe
                                        navController.navigate(RecipeAppScreen.Details.name)
                                    }
                                )

                                AppPage.Settings -> SettingsPage(
                                    showQuickHints = showQuickHints,
                                    onShowQuickHintsChange = { enabled ->
                                        showQuickHints = enabled
                                        onShowQuickHintsChanged(enabled)
                                    },
                                    onCheckForUpdates = onCheckForUpdates,
                                    onMenuClick = { scope.launch { drawerState.open() } }
                                )

                                AppPage.Share -> InfoPage(
                                    title = "اشتراک‌گذاری آشپزیار",
                                    body = "آشپزیار را به دوستان و خانواده معرفی کنید. نسخه‌های رسمی از مخزن AS Team ارائه می‌شوند.",
                                    actionLabel = "اشتراک‌گذاری برنامه",
                                    onAction = onShareApp,
                                    onMenuClick = { scope.launch { drawerState.open() } }
                                )

                                AppPage.About -> InfoPage(
                                    title = "درباره نرم‌افزار",
                                    body = "آشپزیار یک دستیار آشپزی چندسکویی و آفلاین‌محور از AS Team است. نسخه 1.0.0 شامل جست‌وجو، دسته‌بندی، علاقه‌مندی، اطلاعات زمان و سختی، مواد لازم و مراحل پخت است.\n\nDevelop by AS Team Group\nVersion 1.0.0",
                                    actionLabel = "بررسی نسخه جدید",
                                    onAction = onCheckForUpdates,
                                    onMenuClick = { scope.launch { drawerState.open() } }
                                )

                                AppPage.Contact -> InfoPage(
                                    title = "تماس با ما",
                                    body = "پشتیبانی و پیشنهادها:\nAS.Developers.Support@Gmail.Com",
                                    onMenuClick = { scope.launch { drawerState.open() } }
                                )
                            }
                        }
                    }

                    composable(route = RecipeAppScreen.Details.name) {
                        RecipeDetails(
                            animatedVisibilityScope = this,
                            sharedTransactionScope = sharedTransitionScope,
                            isLarge = isLarge,
                            sensorManager = sensorManager,
                            recipe = currentRecipe,
                            goBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

/** Drawer راست‌چین استاندارد AS Team. تنظیمات و اشتراک‌گذاری در ابتدای آیتم‌ها قرار دارند. */
@Composable
private fun AshpazYarDrawer(currentPage: AppPage, onSelect: (AppPage) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(28.dp))
        Box(
            modifier = Modifier.size(76.dp).clip(CircleShape).background(primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "پروفایل",
                modifier = Modifier.size(38.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
        Text("آشپزیار", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Text("AS Team", style = MaterialTheme.typography.caption)
        Spacer(Modifier.height(20.dp))
        Divider()

        DrawerItem("تنظیمات", Icons.Default.Settings, currentPage == AppPage.Settings) { onSelect(AppPage.Settings) }
        DrawerItem("اشتراک‌گذاری", Icons.Default.Share, currentPage == AppPage.Share) { onSelect(AppPage.Share) }
        DrawerItem("خانه", Icons.Default.Home, currentPage == AppPage.Home) { onSelect(AppPage.Home) }
        DrawerItem("علاقه‌مندی‌ها", Icons.Default.Favorite, currentPage == AppPage.Favorites) { onSelect(AppPage.Favorites) }
        DrawerItem("درباره نرم‌افزار", Icons.Default.Info, currentPage == AppPage.About) { onSelect(AppPage.About) }
        DrawerItem("تماس با ما", Icons.Default.ContactMail, currentPage == AppPage.Contact) { onSelect(AppPage.Contact) }
    }
}

/** یک ردیف آیکون‌دار Drawer با نمایش وضعیت انتخاب‌شده. */
@Composable
private fun DrawerItem(title: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 13.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = if (selected) orangeDark else text)
        Text(
            text = title,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) orangeDark else text
        )
    }
}

/** تنظیمات سبک برنامه؛ State آن در Android به شکل پایدار ذخیره می‌شود. */
@Composable
private fun SettingsPage(
    showQuickHints: Boolean,
    onShowQuickHintsChange: (Boolean) -> Unit,
    onCheckForUpdates: () -> Unit,
    onMenuClick: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(sugar).padding(20.dp)) {
        SimpleHeader("تنظیمات", onMenuClick)
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text("راهنمای سریع", fontWeight = FontWeight.Bold)
                Text("نمایش نکته‌های کوتاه آشپزی", style = MaterialTheme.typography.caption)
            }
            Switch(checked = showQuickHints, onCheckedChange = onShowQuickHintsChange)
        }
        Spacer(Modifier.height(16.dp))
        Text("حالت آفلاین فعال است و برای مشاهده دستورهای داخلی به اینترنت نیاز نیست.")
        Spacer(Modifier.height(24.dp))
        Button(onClick = onCheckForUpdates) {
            Text("بررسی نسخه جدید")
        }
    }
}

/** صفحه متنی مشترک برای درباره، اشتراک‌گذاری و تماس. */
@Composable
private fun InfoPage(
    title: String,
    body: String,
    actionLabel: String? = null,
    onAction: () -> Unit = {},
    onMenuClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(sugar).verticalScroll(rememberScrollState()).padding(20.dp)
    ) {
        SimpleHeader(title, onMenuClick)
        Spacer(Modifier.height(24.dp))
        Text(body, style = MaterialTheme.typography.body1)
        if (actionLabel != null) {
            Spacer(Modifier.height(24.dp))
            Button(onClick = onAction) {
                Text(actionLabel)
            }
        }
    }
}

@Composable
private fun SimpleHeader(title: String, onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Text("☰", modifier = Modifier.clickable(onClick = onMenuClick).padding(12.dp), style = MaterialTheme.typography.h5)
    }
}

enum class RecipeAppScreen {
    List, Details,
}
