package com.example.jetcompose.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import coil.compose.rememberImagePainter
import com.example.jetcompose.list.ListFragmentDirections
import com.example.jetcompose.models.DiscoverResults

@Composable
fun ItemImage(
        discoverResults: DiscoverResults,
        width: Int = 120,
        height: Int = 180,
        cornerSize: Int = 0, fragment: Fragment
) {
    Image(
            painter = rememberImagePainter(data = discoverResults.poster_path.srcImagePath,
                    builder = {
                        crossfade(true)
                    }),
            contentDescription = discoverResults.title,
            modifier = Modifier
                    .clickable {
                        val directions = ListFragmentDirections.actionListFragmentToDetailFragment(
                                discoverResults
                        )
                        findNavController(fragment).navigate(directions)
                    }
                    .size(width = width.dp, height = height.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(cornerSize.dp)),
            contentScale = ContentScale.Crop,
    )
}
