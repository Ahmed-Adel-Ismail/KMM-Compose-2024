package home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
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
import home.core.initialize
import home.core.ports.HomeStatePort
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    homeState: HomeStatePort,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val items by homeState.repositories
    val progress by homeState.progress
    val error by homeState.error

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) { homeState.initialize() }
    }
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(title = { Text(text = "Home", modifier = Modifier.fillMaxWidth()) })
        if (progress) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        GithubRepositoriesList(
            items = items,
            error = error,
            modifier = Modifier.background(MaterialTheme.colors.background)
        )
    }

}

@Composable
fun RowScope.GithubRepositoryItemImage(it: String) {
    when (val resource = asyncPainterResource(it)) {
        is Resource.Loading -> GithubRepositoryLoadingImage()
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
        modifier = Modifier.Companion
            .size(80.dp)
            .padding(4.dp)
            .clip(CircleShape)
    )
}

@Composable
fun RowScope.GithubRepositoryErrorImage() {
    Icon(
        Icons.Sharp.Warning,
        tint = MaterialTheme.colors.error,
        contentDescription = null,
        modifier = Modifier.Companion
            .size(80.dp)
            .padding(24.dp)
            .clip(CircleShape)
    )
}

@Composable
fun RowScope.GithubRepositoryLoadingImage() {
    CircularProgressIndicator(
        modifier = Modifier.Companion
            .size(80.dp)
            .padding(24.dp)
            .clip(CircleShape)
    )
}

@Composable
fun GithubRepositoryItem(
    item: GithubRepository,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(80.dp).padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GithubRepositoryItemImage(item.avatarUrl.orEmpty())
        GithubRepositoryItemMiddleSection(item)
        GithubRepositoryItemStarsSection(modifier, item)
    }
}

@Composable
fun RowScope.GithubRepositoryItemStarsSection(modifier: Modifier, item: GithubRepository) {
    Column(
        modifier = modifier.fillMaxHeight().weight(3f),
        horizontalAlignment = Alignment.CenterHorizontally,
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
    items: List<GithubRepository>?,
    error: Throwable?,
    modifier: Modifier = Modifier
) {
    when {
        error != null -> GithubRepositoriesErrorMessage(modifier, error)
        items.isNullOrEmpty() -> GithubRepositoriesEmptyMessage(modifier)
        else -> GithubRepositoriesLoadedView(items, modifier)
    }
}

@Composable
fun GithubRepositoriesLoadedView(
    items: List<GithubRepository>,
    modifier: Modifier
) {
    LazyColumn {
        items(items) { item ->
            GithubRepositoryItem(item = item, modifier = modifier)
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