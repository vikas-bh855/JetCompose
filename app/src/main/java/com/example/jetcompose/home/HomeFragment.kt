package com.example.jetcompose.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.jetcompose.R
import com.example.jetcompose.models.DiscoverResults
import com.example.jetcompose.models.DiscoverResultsParameterProvider
import com.example.jetcompose.models.discoverList
import com.example.jetcompose.profile.ProfileActivity
import com.example.jetcompose.theme.colorAppBackground
import com.example.jetcompose.theme.colorOffWhite
import com.example.jetcompose.theme.colorWhite
import com.example.jetcompose.utils.ItemImage
import com.example.jetcompose.utils.Loader
import com.example.jetcompose.utils.fontFamilyPR
import com.example.jetcompose.utils.srcImagePath
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.absoluteValue

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Home()
            }
        }
    }

    @Composable
    fun Home() {
        var isClick by remember { mutableStateOf(false) }
        val translationAnimation by animateFloatAsState(
            targetValue = if (isClick) 150f else 0f, animationSpec = spring(
                dampingRatio = 0.65f, stiffness = Spring.StiffnessLow
            )
        )
        val rotateZ by animateFloatAsState(
            targetValue = if (isClick) -45f else 0f, animationSpec = tween(300)
        )
        Scaffold(
            containerColor = colorAppBackground,
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = Color(0xFF1d1e33),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                    onClick = {
                        startActivity(Intent(requireContext(), ProfileActivity::class.java))
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .graphicsLayer {
                            translationY = -translationAnimation.times(1.2f)
                        }) {
                    Image(
                        painter = painterResource(id = R.drawable.menu_profile),
                        contentDescription = "",
                    )
                }
                FloatingActionButton(
                    containerColor = Color(0xFF1d1e33),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                    onClick = {},
                    modifier = Modifier
                        .size(50.dp)
                        .graphicsLayer {
                            translationX = -translationAnimation.times(1.2f)
                        }) {
                    Image(
                        painter = painterResource(id = R.drawable.setting), contentDescription = ""
                    )
                }
                FloatingActionButton(
                    containerColor = Color(0xFF1d1e33),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                    onClick = {
                        findNavController().navigate(R.id.searchFragment)
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .graphicsLayer {
                            translationX = -translationAnimation
                            translationY = -translationAnimation
                        }) {
                    Image(
                        painter = painterResource(id = R.drawable.search), contentDescription = ""
                    )
                }
                FloatingActionButton(
                    containerColor = Color(0xFF1d1e33),
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                    onClick = { isClick = !isClick },
                    modifier = Modifier
                        .zIndex(10f)
                        .size(50.dp)
                        .graphicsLayer {
                            rotationZ = rotateZ
                        }) {
                    Image(
                        painter = painterResource(id = R.drawable.add), contentDescription = ""
                    )
                }
            }, floatingActionButtonPosition = FabPosition.End
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val mapDiscover = homeViewModel.listDiscover.value
                val listBanner = homeViewModel.listTrending.collectAsState()
                val listNowPlaying = homeViewModel.listNowPlaying.value
                val listSorted = mapDiscover.keys.sorted().toMutableList()
                listSorted.add(0, "Banner")
                listSorted.add(1, "Now Playing")
                Loader(listNowPlaying.isEmpty())
                LazyColumn {
                    items(listSorted) { genreName ->
                        when (genreName) {
                            "Banner" -> Banner(listBanner.value)
                            "Now Playing" -> NowPlaying(genreName, listNowPlaying)
                            else -> Discover(mapDiscover[genreName]!!, genreName)
                        }
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun Banner(listBanner: List<DiscoverResults> = discoverList) {
        val pagerState = rememberPagerState(pageCount = {
            listBanner.size
        })
        val itemSpacing = 10.dp
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 40.dp, vertical = 7.dp),
            pageSpacing = itemSpacing
        ) { page ->
            Box {
                Card(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset =
                                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                            scaleY = lerp(
                                start = 0.75f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                        .aspectRatio(16 / 9f), shape = RoundedCornerShape(12.dp)) {
                    Box(Modifier.fillMaxSize()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = listBanner[page].backdrop_path.srcImagePath),
                            contentDescription = "Banner Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                    }
                }
            }
        }
    }

    @Composable
    fun NowPlaying(
        title: String = "title", listDiscover: List<DiscoverResults>,
    ) {
        if (listDiscover.isNotEmpty()) {
            Title(title = title, 15)
            LazyRow(Modifier.padding(vertical = 7.dp)) {
                items(listDiscover) { discoverResults ->
                    ItemImage(discoverResults, 150, 230, 12, this@HomeFragment)
                }
            }
        }
    }

    @Composable
    fun Discover(
        listDiscover: List<DiscoverResults>,
        title: String = "title",
        width: Int = 120,
        height: Int = 180
    ) {
        if (listDiscover.isNotEmpty()) {
            val lazyListState = rememberLazyListState()
            if (title.isNotBlank()) Title(title = title, 15)
            LazyRow(
                Modifier.padding(vertical = 7.dp),
                state = lazyListState,
                flingBehavior = ScrollableDefaults.flingBehavior()
            ) {
                items(listDiscover) { discover ->
                    ShowImage(discover, width, height)
                }
            }
        }
    }

    @Composable
    fun ShowImage(
        @PreviewParameter(DiscoverResultsParameterProvider::class) discoverResults: DiscoverResults,
        width: Int = 120,
        height: Int = 180
    ) {
        Column(
            modifier = Modifier
                .width(width.dp)
                .height(height.dp.plus(30.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ItemImage(discoverResults, cornerSize = 12, fragment = this@HomeFragment)
            Text(
                text = discoverResults.title,
                fontFamily = fontFamilyPR,
                fontWeight = FontWeight.ExtraBold,
                color = colorOffWhite,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 5.dp),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    @Composable
    fun Title(title: String, size: Int) {
        Text(
            text = title,
            color = colorWhite,
            fontSize = size.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamilyPR,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
    }

    companion object {
        private const val TAG = "ListFragment"
    }
}



