package favorites.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        (1..10).forEach { index ->
            SwipeCard {
                Text(
                    text = "Swipe left to delete item $index",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize().background(
                        MaterialTheme.colors.secondary
                    )
                )
            }

        }
    }

}