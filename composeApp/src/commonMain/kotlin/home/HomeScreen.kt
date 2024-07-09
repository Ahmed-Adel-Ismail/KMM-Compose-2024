package home

import aismailproject.composeapp.generated.resources.Res
import aismailproject.composeapp.generated.resources.compose_multiplatform
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import home.core.GithubRepository
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen() {
    GithubRepositoriesList(items = (1..100).map { githubRepository(it) })
}

@Composable
private fun githubRepository(id: Int = 0) = GithubRepository(
    metadata = id,
    id = id.toLong(),
    name = "Github Repository $id",
    ownerName = "Github Owner $id",
    avatarUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fstock.adobe.com%2Fsearch%3Fk%3Dexample&psig=AOvVaw0Tjjyz91203DRbc-wbJ6qy&ust=1720648790327000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCJCgvbL6mocDFQAAAAAdAAAAABAE",
    stargazersCount = id * 1000
)


@Composable
fun GithubRepositoryItem(item: GithubRepository, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(80.dp)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.compose_multiplatform), // TODO: load with Kamel
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(3f)
                .clip(CircleShape)

        )
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
fun GithubRepositoriesList(items: List<GithubRepository>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(items) { item ->
            GithubRepositoryItem(item = item, modifier = modifier)
            Divider()
        }
    }
}