package com.example.jetcompose.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.jetcompose.models.DiscoverResults

@Composable
fun ItemImage(
        discoverResults: DiscoverResults,
        width: Int = 120,
        height: Int = 180,
        cornerSize: Int = 0, fragment: Fragment
) {
    AsyncImage(model = ImageRequest.Builder(LocalContext.current)
            .data(discoverResults.poster_path.srcImagePath)
            .crossfade(true).build(),
            contentDescription = discoverResults.title,
            modifier = Modifier
                    .clickable {
                      /*  val directions = ListFragmentDirections.actionListFragmentToDetailFragment(
                                discoverResults
                        )
                        findNavController(fragment).navigate(directions)*/
                    }
                    .size(width = width.dp, height = height.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(cornerSize.dp)),
            contentScale = ContentScale.Crop,
    )
}
