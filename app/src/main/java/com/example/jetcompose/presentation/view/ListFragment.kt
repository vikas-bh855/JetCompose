package com.example.jetcompose.presentation.view

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import coil.Coil
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.jetcompose.R
import com.example.jetcompose.data.models.DiscoverParameterProvider
import com.example.jetcompose.data.models.DiscoverResults
import com.example.jetcompose.data.models.DiscoverResultsParameterProvider
import com.example.jetcompose.fontFamilyPR
import com.example.jetcompose.presentation.utils.Loader
import com.example.jetcompose.presentation.viewmodel.ListViewModel
import com.example.jetcompose.srcImagePath
import com.example.jetcompose.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue


@ExperimentalPagerApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class ListFragment : Fragment() {
    private val listViewModel by viewModels<ListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
            duration = 200
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                isTransitionGroup = true
                Home()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Home() {
        var isClick by remember { mutableStateOf(false) }
        val translationAnimation by animateFloatAsState(
            targetValue = if (isClick) 150f else 0f,
            animationSpec = spring(
                dampingRatio = 0.65f,
                stiffness = Spring.StiffnessLow
            )
        )
        val rotateZ by animateFloatAsState(
            targetValue = if (isClick) -45f else 0f,
            animationSpec = tween(300)
        )
        var color by remember {
            mutableStateOf(0x00000000)
        }
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = colorAppBackground,
                    shape = CircleShape,
                    onClick = {
                        startActivity(
                            Intent(requireContext(), ProfileActivity::class.java)
                        )
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
                    backgroundColor = colorAppBackground,
                    shape = CircleShape,
                    onClick = {},
                    modifier = Modifier
                        .size(50.dp)
                        .graphicsLayer {
                            translationX = -translationAnimation.times(1.2f)
                        }) {
                    Image(
                        painter = painterResource(id = R.drawable.setting),
                        contentDescription = ""
                    )

                }
                FloatingActionButton(
                    backgroundColor = colorAppBackground,
                    shape = CircleShape,
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
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = ""
                    )
                }
                FloatingActionButton(backgroundColor = colorDarkGrey,
                    shape = CircleShape,
                    onClick = { isClick = !isClick },
                    modifier = Modifier
                        .zIndex(10f)
                        .size(50.dp)
                        .graphicsLayer {
                            rotationZ = rotateZ
                        }) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "",
                        tint = Color(0xFFFDF8F2),
                        modifier = Modifier.size(35.dp)
                    )
                }
            },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.End,
            backgroundColor = Color(color).copy(alpha = 0.2f)
        )
        {
            val mapDiscover = listViewModel.listDiscover.value.toSortedMap()
            val listBanner = listViewModel.listTrending.collectAsState()
            val listNowPlaying = listViewModel.listNowPlaying.value
            val listSorted = mapDiscover.keys.sorted().toMutableList()
            listSorted.add(0, "Banner")
            listSorted.add(1, "Now Playing")
            Loader(listNowPlaying.isEmpty())
            LazyColumn {
                items(listSorted) { genreName ->
                    when (genreName) {
                        "Banner" -> Banner(listBanner.value) {
                            color = it
                        }
                        "Now Playing" -> NowPlaying(genreName, listNowPlaying)
                        else -> Discover(genreName, mapDiscover[genreName]!!)
                    }
                }
            }
        }
    }

    private var isLoaded = 0

    @Preview
    @Composable
    fun Banner(
        @PreviewParameter(DiscoverResultsParameterProvider::class) listBanner: List<DiscoverResults>,
        color: (Int) -> Unit = {}
    ) {
//        val pagerState = rememberPagerState()
//        if (listBanner.isNotEmpty()) {
//            val url = listBanner[pagerState.currentPage].backdrop_path.srcImagePath
//            LaunchedEffect(key1 = url) {
//                val request = ImageRequest.Builder(requireContext())
//                    .data(url)
//                    .size(128)
//                    .allowHardware(false).build()
//                val bitmap =
//                    ImageLoader(requireContext()).execute(request).drawable?.toBitmap()!!
//                withContext(Dispatchers.Default) {
//                    Palette.from(bitmap).maximumColorCount(4)
//                        .generate { it ->
//                            it?.swatches?.let { swatch ->
//                                swatch.sortedByDescending {
//                                    it.population
//                                }
//                            }?.let {
//                                color(it[0].rgb)
//                            }
//                        }
//                }
//            }
//        }
        HorizontalPager(
            itemSpacing = (-40).dp,
            count = listBanner.size,
            contentPadding = PaddingValues(horizontal = 30.dp),
            modifier = Modifier.padding(top = 30.dp, bottom = 10.dp)
        ) { pager ->
            BoxWithConstraints {
                Card(
                    backgroundColor = Color.Transparent,
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = calculateCurrentOffsetForPage(pager).absoluteValue
                            val dp = lerp(
                                start = 2f.toDp(),
                                stop = 2.6f.toDp(),
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                            scaleX = dp.value
                            scaleY = dp.value
                        }
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f), elevation = 10.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = listBanner[pager].backdrop_path.srcImagePath),
                            contentDescription = "Banner Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            text = listBanner[pager].title ?: "",
                            fontFamily = fontFamilyPR,
                            color = Color.White,
                            modifier = Modifier
                                .padding(5.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    color = colorDarkGreyTransparent,
                                    shape = RoundedCornerShape(10.dp),
                                )
                                .padding(5.dp),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

        }
    }


    @Preview
    @Composable
    fun NowPlaying(
        title: String = "title",
        @PreviewParameter(DiscoverParameterProvider::class) listDiscover: List<DiscoverResults>,
    ) {
        if (listDiscover.isNotEmpty()) {
            Title(title = title, 15)
            LazyRow(Modifier.padding(vertical = 7.dp)) {
                items(listDiscover) { discoverResults ->
                    Image(discoverResults, 150, 230, 12)
                }
            }
        }
    }

    @Preview
    @Composable
    fun Discover(
        title: String = "title", listDiscover: List<DiscoverResults> = emptyList(),
        width: Int = 120, height: Int = 180
    ) {
        if (listDiscover.isNotEmpty()) {
            val lazyListState = rememberLazyListState()
            if (title.isNotBlank())
                Title(title = title, 15)
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

    @Preview
    @Composable
    fun ShowImage(
        @PreviewParameter(DiscoverResultsParameterProvider::class)
        discoverResults: DiscoverResults,
        width: Int = 120, height: Int = 180
    ) {
        Column(
            modifier = Modifier
                .width(width.dp)
                .height(height.dp.plus(30.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(discoverResults)
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

    @Composable
    fun Image(
        discoverResults: DiscoverResults,
        width: Int = 120,
        height: Int = 180,
        cornerSize: Int = 0
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
                    findNavController().navigate(directions)
                }
                .size(width = width.dp, height = height.dp)
                .padding(5.dp)
                .clip(RoundedCornerShape(cornerSize.dp)),
            contentScale = ContentScale.Crop,
        )
    }

    companion object {
        private const val TAG = "ListFragment"
    }
}


