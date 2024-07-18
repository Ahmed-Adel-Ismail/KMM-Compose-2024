package favorites.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import favorites.adapters.FavoriteState
import favorites.core.entities.FavoriteRepository
import favorites.core.ports.AllFavoritesStatePort
import favorites.core.ports.FavoriteStatePort
import favorites.core.scenarios.listAll
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

private val imageModifier =
    Modifier.size(width = 70.dp, height = 76.dp).padding(4.dp).clip(CircleShape)

private val imageProgressModifier =
    Modifier.size(width = 70.dp, height = 76.dp).padding(20.dp).clip(CircleShape)

@Composable
fun FavoritesScreen(
    state: AllFavoritesStatePort,
    modifier: Modifier = Modifier,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    imageLoader: @Composable (String) -> Resource<Painter> = { asyncPainterResource(it) },
    onBackPress: () -> Unit,
) {

    val scope = rememberCoroutineScope()

    @Composable
    fun TopAppBarContent() {
        TopAppBar(title = { Text(text = "Favorites", modifier = Modifier.fillMaxWidth()) },
            navigationIcon = {
                IconButton(onClick = onBackPress) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            })
    }

    @Composable
    fun ListItemsContent(
        items: List<FavoriteStatePort>?,
        error: Throwable?,
        onItemClicked: (FavoriteStatePort) -> Unit = {}
    ) {

        @Composable
        fun ErrorContent(error: Throwable) {

            @Composable
            fun ErrorText(error: Throwable) {
                Text(
                    text = "An error occurred: ${error.message}",
                    style = MaterialTheme.typography.subtitle1
                )
            }

            @Composable
            fun ErrorIcon() {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier.size(64.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ErrorIcon()
                ErrorText(error)
            }
        }

        @Composable
        fun EmptyContent() {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No Favorites", style = MaterialTheme.typography.h6)
            }
        }

        @Composable
        fun LoadedContent(
            listItems: List<FavoriteStatePort>, onItemClicked: (FavoriteStatePort) -> Unit
        ) {

            @Composable
            fun ItemContent(
                item: FavoriteStatePort, onItemClicked: (FavoriteStatePort) -> Unit
            ) {

                @Composable
                fun ItemImageSection(imageUrl: String) {

                    @Composable
                    fun FetchedImage(painter: Painter) {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = imageModifier
                        )
                    }

                    @Composable
                    fun ErrorImage() {
                        Icon(
                            Icons.Sharp.Warning,
                            tint = MaterialTheme.colors.error,
                            contentDescription = null,
                            modifier = imageModifier
                        )
                    }

                    when (val resource = imageLoader(imageUrl)) {
                        is Resource.Loading -> CircularProgressIndicator(modifier = imageProgressModifier)
                        is Resource.Success -> FetchedImage(resource.value)
                        is Resource.Failure -> ErrorImage()
                    }
                }

                @Composable
                fun RowScope.ItemMiddleSection(githubRepository: FavoriteRepository) {
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(10f).padding(8.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = githubRepository.name.orEmpty(),
                            style = MaterialTheme.typography.subtitle1
                        )
                        Text(
                            text = githubRepository.ownerName.orEmpty(),
                            style = MaterialTheme.typography.body2
                        )
                        Icon(
                            Icons.Filled.Favorite,
                            tint = MaterialTheme.colors.primary,
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }

                @Composable
                fun RowScope.ItemStarsSection(githubRepository: FavoriteRepository) {
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(3f).padding(4.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Icon(
                            if (item.progress) Icons.Filled.Lock else Icons.Filled.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = githubRepository.stargazersCount.toString(),
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }

                Row(
                    modifier = modifier.height(80.dp).padding(4.dp)
                        .clickable { onItemClicked(item) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ItemImageSection(item.favoriteRepository.avatarUrl.orEmpty())
                    ItemMiddleSection(item.favoriteRepository)
                    ItemStarsSection(item.favoriteRepository)
                }
            }

            LazyColumn {
                items(items = listItems, key = { it.favoriteRepository.id }) { item ->
                    ItemContent(item = item, onItemClicked)
                    Divider()
                }
            }
        }

        when {
            error != null -> ErrorContent(error)
            items.isNullOrEmpty() -> EmptyContent()
            else -> LoadedContent(items, onItemClicked)
        }
    }


    @Composable
    fun FavoritesScreenContent() {

        LaunchedEffect(Unit) {
            withContext(ioDispatcher) {
                state.listAll { FavoriteState(it) }
            }
        }

        Column(modifier = modifier.fillMaxSize()) {
            TopAppBarContent()
            if (state.progress) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            ListItemsContent(
                items = state.favorites, error = state.error
            )
        }
    }

    FavoritesScreenContent()
}

