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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
import com.example.jetcompose.models.discoverList
import com.example.jetcompose.profile.ProfileActivity
import com.example.jetcompose.theme.colorAppBackground
import com.example.jetcompose.theme.colorBlue
import com.example.jetcompose.theme.colorLightGrey
import com.example.jetcompose.theme.colorWhite
import com.example.jetcompose.utils.FAB
import com.example.jetcompose.utils.ItemImage
import com.example.jetcompose.utils.Loader
import com.example.jetcompose.utils.bannerImagePath
import com.example.jetcompose.utils.fontFamilyPR
import com.example.jetcompose.utils.genresList
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
                val genresList = homeViewModel.listDiscover.collectAsState()
                val listBanner = homeViewModel.listTrending.collectAsState()
                val listNowPlaying = homeViewModel.listNowPlaying.collectAsState()
                Home(genresList.value, listBanner.value, listNowPlaying.value)
            }
        }
    }

    @Preview
    @Composable
    fun Home(
        mapDiscover: Map<String, List<DiscoverResults>> = mapOf(Pair("Action", discoverList)),
        listBanner: List<DiscoverResults> = discoverList,
        listNowPlaying: List<DiscoverResults> = discoverList
    ) {
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
            containerColor = colorAppBackground, floatingActionButton = {
                FAB({
                    startActivity(Intent(requireContext(), ProfileActivity::class.java))
                }, R.drawable.profile) {
                    translationY = -translationAnimation.times(1.2f)
                }
                FAB({}, R.drawable.setting) {
                    translationX = -translationAnimation.times(1.2f)
                }
                FAB(onClick = {
                    findNavController().navigate(R.id.searchFragment)
                }, R.drawable.search) {
                    translationX = -translationAnimation
                    translationY = -translationAnimation
                }
                FAB({ isClick = !isClick }, R.drawable.home) {
                    rotationZ = rotateZ
                }
            }, floatingActionButtonPosition = FabPosition.End
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val listSorted = mapDiscover.keys.sorted().toMutableList()
                listSorted.add(0, "Banner")
                listSorted.add(1, "NOW PLAYING")
                Loader(listNowPlaying.isEmpty())
                LazyColumn {
                    items(listSorted) { genreName ->
                        when (genreName) {
                            "Banner" -> Banner(listBanner)
                            "NOW PLAYING" -> NowPlaying(genreName, listNowPlaying)
                            else -> Discover(mapDiscover[genreName]!!, genreName)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun width(): Dp {
        val localDensity = LocalDensity.current
        return with(localDensity) {
            LocalResources.current.displayMetrics.widthPixels.toDp()
        }
    }

    @Composable
    fun Banner(listBanner: List<DiscoverResults> = discoverList) {
        Spacer(Modifier.padding(top = 10.dp))
        Title("TRENDING", 18, colorBlue)
        if (listBanner.isNotEmpty()) {
            val pagerState = rememberPagerState(pageCount = {
                listBanner.size
            }, initialPage = listBanner.size / 2)
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = width() / 2 - 125.dp, vertical = 7.dp),
                pageSpacing = (-15).dp,
            ) { page ->
                Box(
                    modifier = Modifier
                        .size(250.dp, 250.dp)
                        .graphicsLayer {
                            val pageOffset =
                                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                            alpha = lerp(
                                start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                            scaleY = lerp(
                                start = 0.75f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                            scaleX = lerp(
                                start = 0.75f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = listBanner[page].backdrop_path.bannerImagePath),
                        contentDescription = "Banner Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                    )
                }
            }
            Text(
                text = listBanner[pagerState.currentPage].title,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamilyPR,
                color = colorWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth()
                    .graphicsLayer {
                        val offset = pagerState.currentPageOffsetFraction.absoluteValue
                        alpha = offset * (-2) + 1
                    })

            Text(
                text = genresList.genres.first { genre -> listBanner[pagerState.currentPage].genre_ids.any { it == genre.id.toInt() } }.name,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamilyPR,
                color = colorLightGrey,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth()
                    .graphicsLayer {
                        val offset = pagerState.currentPageOffsetFraction.absoluteValue
                        alpha = offset * (-2) + 1
                    })
        }
    }

    @Composable
    fun NowPlaying(
        title: String = "title", listDiscover: List<DiscoverResults>,
    ) {
        if (listDiscover.isNotEmpty()) {
            Spacer(modifier = Modifier.padding(top = 15.dp))
            Title(title, 18, colorBlue)
            val pagerState = rememberPagerState(
                pageCount = { listDiscover.size },
                initialPage = listDiscover.size / 2
            )
            val coroutineScope = rememberCoroutineScope()
            HorizontalPager(
                contentPadding = PaddingValues(horizontal = (width() / 2) - 75.dp),
                state = pagerState,
                pageSpacing = (-110).dp,
                pageSize = PageSize.Fixed(180.dp)
            ) { page ->
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset =
                                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                            val linearOffset = lerp(
                                start = 0.65f, stop = 1f, fraction = 1f - pageOffset
                            )
                            alpha = linearOffset
                            scaleY = linearOffset
                            scaleX = linearOffset
                            rotationZ = pagerState.getOffsetDistanceInPages(page) * 10
                        }
                        .zIndex(
                            if (pagerState.currentPage == page) 2f else {
                                1 - (pagerState.getOffsetDistanceInPages(page).absoluteValue / listDiscover.size)
                            }
                        )) {
                    ItemImage(listDiscover[page], 160, 240, 20, this@HomeFragment)
                }
            }
        }
    }

    @Composable
    fun Discover(
        listDiscover: List<DiscoverResults>,
        title: String = "title",
    ) {
        if (listDiscover.isNotEmpty()) {
            val lazyListState = rememberLazyListState()
            if (title.isNotBlank()) Title(title = title, 15)
            LazyRow(
                Modifier.padding(vertical = 7.dp),
                state = lazyListState
            ) {
                items(listDiscover) { discover ->
                    ItemImage(discover, cornerSize = 12, fragment = this@HomeFragment)
                }
            }
        }
    }

    @Composable
    fun Title(title: String, size: Int, color: Color = colorWhite) {
        Text(
            text = title,
            color = color,
            fontSize = size.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = fontFamilyPR,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
    }

    companion object {
        private const val TAG = "ListFragment"
    }
}


