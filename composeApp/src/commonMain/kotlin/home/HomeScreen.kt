package home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import home.core.GithubRepository
import home.core.SelectableGithubRepository
import home.core.fetch
import home.core.initialize
import home.core.ports.HomeStatePort
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
    homeState: HomeStatePort,
    modifier: Modifier = Modifier,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    onFavoritesClicked: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val items = homeState.selectableRepositories
    val progress by homeState.progress
    val error by homeState.genericError

    LaunchedEffect(Unit) {
        withContext(dispatcher){
            homeState.fetch()
        }
    }

    homeState.initialize()
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "Home", modifier = Modifier.fillMaxWidth()) },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colors.background
                    )
                }
            }
        )
        if (progress) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        GithubRepositoriesList(
            items = items,
            error = error,
            modifier = Modifier.background(MaterialTheme.colors.background),
            onItemClicked = { scope.launch(dispatcher) {} }
        )
    }

}

@Composable
fun RowScope.GithubRepositoryItemImage(it: String) {
    when (val resource = asyncPainterResource(it)) {
        is Resource.Loading -> CircularProgressIndicator(modifier = imageProgressModifier)
        is Resource.Success -> GithubRepositoryFetchedImage(resource.value)
        is Resource.Failure -> GithubRepositoryErrorImage()
    }
}

@Composable
fun RowScope.GithubRepositoryFetchedImage(painter: Painter) {
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = imageModifier
    )
}

@Composable
fun RowScope.GithubRepositoryErrorImage() {
    Icon(
        Icons.Sharp.Warning,
        tint = MaterialTheme.colors.error,
        contentDescription = null,
        modifier = imageModifier
    )
}


@Composable
fun GithubRepositoryItem(
    item: SelectableGithubRepository,
    modifier: Modifier = Modifier,
    onItemClicked: (GithubRepository) -> Unit
) {
    Row(
        modifier = modifier.height(80.dp)
            .padding(4.dp)
            .clickable { onItemClicked(item.repository) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GithubRepositoryItemImage(item.repository.avatarUrl.orEmpty())
        GithubRepositoryItemMiddleSection(item.repository)
        GithubRepositoryItemStarsSection(item.repository)
    }
}

@Composable
fun RowScope.GithubRepositoryItemStarsSection(item: GithubRepository) {
    Column(
        modifier = Modifier.fillMaxHeight().weight(3f).padding(4.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            Icons.Filled.Star,
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(text = item.stargazersCount.toString(), style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun RowScope.GithubRepositoryItemMiddleSection(item: GithubRepository) {
    Column(
        modifier = Modifier.fillMaxHeight().weight(10f).padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = item.name.orEmpty(), style = MaterialTheme.typography.subtitle1)
        Text(text = item.ownerName.orEmpty(), style = MaterialTheme.typography.body2)
    }
}

@Composable
fun GithubRepositoriesList(
    items: List<SelectableGithubRepository>?,
    error: Throwable?,
    modifier: Modifier = Modifier,
    onItemClicked: (GithubRepository) -> Unit
) {
    when {
        error != null -> GithubRepositoriesErrorMessage(modifier, error)
        items.isNullOrEmpty() -> GithubRepositoriesEmptyMessage(modifier)
        else -> GithubRepositoriesLoadedView(items, modifier, onItemClicked)
    }
}

@Composable
fun GithubRepositoriesLoadedView(
    items: List<SelectableGithubRepository>,
    modifier: Modifier,
    onItemClicked: (GithubRepository) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            GithubRepositoryItem(item = item, modifier = modifier, onItemClicked)
            Divider()
        }
    }
}

@Composable
fun GithubRepositoriesEmptyMessage(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No repositories", style = MaterialTheme.typography.h6)
    }
}

@Composable
fun GithubRepositoriesErrorMessage(modifier: Modifier, error: Throwable) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.Warning,
            contentDescription = null,
            tint = MaterialTheme.colors.error,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = "An error occurred: ${error.message}",
            style = MaterialTheme.typography.subtitle1
        )
    }
}