package com.example.jetcompose.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.jetcompose.R
import com.example.jetcompose.home.HomeFragmentDirections
import com.example.jetcompose.models.DiscoverResults
import com.example.jetcompose.models.MovieCrew
import com.example.jetcompose.theme.colorAppBackground
import com.example.jetcompose.theme.colorBlue
import com.example.jetcompose.theme.colorFab

@Composable
fun ItemImage(
    discoverResults: DiscoverResults,
    width: Int = 120,
    height: Int = 180,
    cornerSize: Int = 0,
    fragment: Fragment? = null
) {
    //To debug image loading use - LocalContext.current.imageLoader.newBuilder().logger(DebugLogger())
    AsyncImage(
        placeholder = painterResource(R.drawable.placeholder),
        model = ImageRequest.Builder(LocalContext.current)
            .data(discoverResults.poster_path.srcImagePath).crossfade(true).build(),
        contentDescription = discoverResults.title,
        modifier = Modifier
            .clickable {
                if (fragment != null) {
                    val directions = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                        discoverResults
                    )
                    findNavController(fragment).navigate(directions)
                }
            }
            .size(width = width.dp, height = height.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(cornerSize.dp)),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ItemCrew(item: MovieCrew) {
    AsyncImage(
        modifier = Modifier
            .width(80.dp).aspectRatio(12/16f)
            .clip(RoundedCornerShape(10.dp)),
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = item.profile_path?.srcImagePath)
            .apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
            }).build(),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.placeholder)
    )
}

@Composable
fun FAB(onClick: () -> Unit, drawable: Int, graphicsLayer: GraphicsLayerScope.() -> Unit) {
    FloatingActionButton(
        containerColor = colorFab,
        shape = CircleShape,
        onClick = onClick::invoke,
        modifier = Modifier
            .size(50.dp)
            .graphicsLayer {
                graphicsLayer.invoke(this)
            }) {
        Image(
            painter = painterResource(id = drawable), contentDescription = ""
        )
    }
}



