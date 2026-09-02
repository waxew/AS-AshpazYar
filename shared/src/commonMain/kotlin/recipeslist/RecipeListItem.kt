package recipeslist

// کارت نمایشی هر دستور غذا؛ اطلاعات کلیدی و کنترل علاقه‌مندی را بدون خروج از فهرست نشان می‌دهد.

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.Recipe

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeListItem(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    recipe: Recipe,
    isFavorite: Boolean,
    onToggleFavorite: (recipe: Recipe) -> Unit,
    onClick: (recipe: Recipe) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .aspectRatio(1.45f)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(30.dp),
                clip = true,
                ambientColor = Color(0x55CE5A01),
                spotColor = Color(0x55CE5A01)
            )
            .background(recipe.bgColor, RoundedCornerShape(30.dp))
            .clickable { onClick(recipe) }
    ) {
        with(sharedTransitionScope) {
            Card(
                backgroundColor = recipe.bgColor,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .sharedElement(
                        rememberSharedContentState(key = "item-container-${recipe.id}"),
                        animatedVisibilityScope,
                    )
            ) {
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.45f)) {
                    Column(
                        modifier = Modifier.fillMaxHeight().fillMaxWidth(0.60f).padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = recipe.category,
                                style = MaterialTheme.typography.caption,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.weight(1f))
                        }

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = recipe.title,
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState(key = "item-title-${recipe.id}"),
                                animatedVisibilityScope,
                            )
                        )

                        Text(
                            recipe.description,
                            style = MaterialTheme.typography.body2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 6.dp).sharedElement(
                                rememberSharedContentState(key = "recipe-description-${recipe.id}"),
                                animatedVisibilityScope,
                            )
                        )

                        Text(
                            "${recipe.totalMinutes} دقیقه  •  ${recipe.difficulty}  •  ${recipe.servings} نفر",
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    RecipeListItemImageWrapper(
                        modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth(0.43f).aspectRatio(1f),
                        child = {
                            RecipeImage(
                                imageBitmap = recipe.image,
                                modifier = Modifier.sharedElement(
                                    rememberSharedContentState(key = "item-image-${recipe.id}"),
                                    animatedVisibilityScope,
                                )
                            )
                        }
                    )
                }
            }
        }

        IconButton(
            onClick = { onToggleFavorite(recipe) },
            modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "حذف از علاقه‌مندی" else "افزودن به علاقه‌مندی"
            )
        }
    }
}
