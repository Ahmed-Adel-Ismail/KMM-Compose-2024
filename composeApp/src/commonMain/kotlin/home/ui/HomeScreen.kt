package home.ui

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
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import home.adapters.GithubRepositoryState
import home.core.entities.GithubRepository
import home.core.ports.GithubRepositoryStatePort
import home.core.ports.HomeStatePort
import home.core.scenarios.addToFavorites
import home.core.scenarios.fetchAllFavorites
import home.core.scenarios.fetchAllRepositories
import home.core.scenarios.initialize
import home.core.scenarios.merge
import home.core.scenarios.removeFromFavorites
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private val imageModifier = Modifier
    .size(width = 70.dp, height = 76.dp)
    .padding(4.dp)
    .clip(CircleShape)

private val imageProgressModifier = Modifier
    .size(width = 70.dp, height = 76.dp)
    .padding(20.dp)
    .clip(CircleShape)


@Composable
fun HomeScreen(
    state: HomeStatePort,
    modifier: Modifier = Modifier,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    imageLoader: @Composable (String) -> Resource<Painter> = { asyncPainterResource(it) },
    onFavoritesClicked: () -> Unit
) {

    val scope = rememberCoroutineScope()
    var favoriteDialogItem: GithubRepositoryStatePort? by remember { mutableStateOf(null) }

    @Composable
    fun TopAppBarContent() {

        @Composable
        fun FavoritesIconButton() {
            IconButton(onClick = { onFavoritesClicked() }) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colors.background
                )
            }
        }

        TopAppBar(
            title = { Text(text = "Home", modifier = Modifier.fillMaxWidth()) },
            actions = { FavoritesIconButton() }
        )
    }

    @Composable
    fun ListItemsContent(
        items: List<GithubRepositoryStatePort>?,
        error: Throwable?,
        onItemClicked: (GithubRepositoryStatePort) -> Unit
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
                Text(text = "No repositories", style = MaterialTheme.typography.h6)
            }
        }

        @Composable
        fun LoadedContent(
            listItems: List<GithubRepositoryStatePort>,
            onItemClicked: (GithubRepositoryStatePort) -> Unit
        ) {

            @Composable
            fun ItemContent(
                item: GithubRepositoryStatePort,
                onItemClicked: (GithubRepositoryStatePort) -> Unit
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
                fun RowScope.ItemMiddleSection(githubRepository: GithubRepository) {
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
                        if (item.isFavorite) Icon(
                            Icons.Filled.Favorite,
                            tint = MaterialTheme.colors.primary,
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }

                @Composable
                fun RowScope.ItemStarsSection(githubRepository: GithubRepository) {
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
                    modifier = modifier.height(80.dp)
                        .padding(4.dp)
                        .clickable { onItemClicked(item) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ItemImageSection(item.repository.avatarUrl.orEmpty())
                    ItemMiddleSection(item.repository)
                    ItemStarsSection(item.repository)
                }
            }

            LazyColumn {
                items(items = listItems, key = { it.repository.id }) { item ->
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
    fun FavoritesDialog() {

        @Composable
        fun ConfirmButton() {

            suspend fun onConfirmClicked() {
                favoriteDialogItem?.apply {
                    favoriteDialogItem = null
                    if (isFavorite) removeFromFavorites() else addToFavorites()
                }
            }

            Button(onClick = { scope.launch(dispatcher) { onConfirmClicked() } }) {
                Text(text = if (favoriteDialogItem?.isFavorite == true) "Remove" else "Add")
            }
        }

        @Composable
        fun TextContent() {
            Text(
                text = "Are you sure you want to " +
                        "${if (favoriteDialogItem?.isFavorite == true) "remove" else "add"} " +
                        "repository ${favoriteDialogItem?.repository?.name} to your favorites?"
            )
        }

        AlertDialog(
            onDismissRequest = { favoriteDialogItem = null },
            title = { Text(text = "Favorites") },
            text = { TextContent() },
            confirmButton = { ConfirmButton() },
            dismissButton = { Button(onClick = { favoriteDialogItem = null }) { Text("Cancel") } }
        )
    }

    @Composable
    fun HomeScreenContent() {

        LaunchedEffect(Unit) {
            withContext(dispatcher) {
                state.initialize()
                val repositories = async(dispatcher) { state.fetchAllRepositories() }
                val favorites = async(dispatcher) { state.fetchAllFavorites() }
                state.merge(repositories.await(), favorites.await()) { GithubRepositoryState(it) }
            }
        }

        if (favoriteDialogItem != null) FavoritesDialog()

        Column(modifier = modifier.fillMaxSize()) {
            TopAppBarContent()
            if (state.progress) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            ListItemsContent(
                items = state.repositories,
                error = state.error,
                onItemClicked = { item -> if (!item.progress) favoriteDialogItem = item }
            )
        }
    }

    HomeScreenContent()
}
