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
import home.core.ports.GithubRepositoryStatePort
import home.core.ports.HomeStatePort
import home.core.scenarios.addToFavorites
import home.core.scenarios.listAll
import home.core.scenarios.removeFromFavorites
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
    fun ListItemsContent() {

        @Composable
        fun ErrorContent() {

            @Composable
            fun ErrorText() {
                Text(
                    text = "An error occurred: ${state.error?.message}",
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
                ErrorText()
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
        fun LoadedContent() {

            @Composable
            fun ItemContent(item: GithubRepositoryStatePort) {

                @Composable
                fun ItemImageSection() {

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

                    when (val resource = imageLoader(item.repository.avatarUrl.orEmpty())) {
                        is Resource.Loading -> CircularProgressIndicator(modifier = imageProgressModifier)
                        is Resource.Success -> FetchedImage(resource.value)
                        is Resource.Failure -> ErrorImage()
                    }
                }

                @Composable
                fun RowScope.ItemMiddleSection() {
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(10f).padding(8.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = item.repository.name.orEmpty(),
                            style = MaterialTheme.typography.subtitle1
                        )
                        Text(
                            text = item.repository.ownerName.orEmpty(),
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
                fun RowScope.ItemStarsSection() {

                    @Composable
                    fun StarIcon() {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxHeight().weight(3f).padding(4.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (item.progress) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        else StarIcon()
                    }
                    Text(
                        text = item.repository.stargazersCount.toString(),
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                Row(
                    modifier = modifier.height(80.dp)
                        .padding(4.dp)
                        .clickable { if (!item.progress) favoriteDialogItem = item },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ItemImageSection()
                    ItemMiddleSection()
                    ItemStarsSection()
                }
                Divider()
            }

            LazyColumn {
                items(items = state.repositories, key = { it.repository.id }) {
                    ItemContent(item = it)
                }
            }
        }

        when {
            state.error != null -> ErrorContent()
            state.repositories.isEmpty() -> EmptyContent()
            else -> LoadedContent()
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
                state.listAll(dispatcher) { GithubRepositoryState(it) }
            }
        }

        if (favoriteDialogItem != null) FavoritesDialog()

        Column(modifier = modifier.fillMaxSize()) {
            TopAppBarContent()
            if (state.progress) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            ListItemsContent()
        }
    }

    HomeScreenContent()
}
