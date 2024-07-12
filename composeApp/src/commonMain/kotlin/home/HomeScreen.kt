package home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import home.core.GithubRepository
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource

@Composable
fun HomeScreen() {
    GithubRepositoriesList(
        modifier = Modifier.background(MaterialTheme.colors.background),
        items = (1..100).map { githubRepository(it) }) {
        when (val resource = asyncPainterResource(it)) {
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .weight(3f)
                        .padding(24.dp)
                        .clip(CircleShape)
                )
            }

            is Resource.Success -> {
                val painter: Painter = resource.value
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(3f)
                        .clip(CircleShape)
                )
            }

            is Resource.Failure -> {
                Icon(
                    Icons.Sharp.Warning,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(3f)
                        .padding(24.dp)
                        .clip(CircleShape)

                )
            }
        }
    }
}

@Composable
private fun githubRepository(id: Int = 0) = GithubRepository(
    metadata = id,
    id = id.toLong(),
    name = "Github Repository $id",
    ownerName = "Github Owner $id",
    avatarUrl = "https://picsum.photos/200/300",
    stargazersCount = id * 1000
)


@Composable
fun GithubRepositoryItem(
    item: GithubRepository,
    modifier: Modifier = Modifier,
    imageContent: @Composable RowScope.(imageUrl: String) -> Unit
) {
    Row(
        modifier = modifier
            .height(80.dp)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        imageContent(item.avatarUrl.orEmpty())
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(10f)
                .padding(4.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = item.name.orEmpty(), style = MaterialTheme.typography.subtitle1)
            Text(text = item.ownerName.orEmpty(), style = MaterialTheme.typography.body2)
        }
        Column(
            modifier = modifier
                .fillMaxHeight()
                .weight(3f),
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
}

@Composable
fun GithubRepositoriesList(
    items: List<GithubRepository>,
    modifier: Modifier = Modifier,
    imageContent: @Composable RowScope.(imageUrl: String) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            GithubRepositoryItem(item = item, modifier = modifier, imageContent)
            Divider()
        }
    }
}