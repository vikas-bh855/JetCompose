package com.example.jetcompose.list

import android.content.Intent
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
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import com.example.jetcompose.R
import com.example.jetcompose.models.DiscoverParameterProvider
import com.example.jetcompose.models.DiscoverResults
import com.example.jetcompose.models.DiscoverResultsParameterProvider
import com.example.jetcompose.profile.ProfileActivity
import com.example.jetcompose.theme.*
import com.example.jetcompose.utils.ItemImage
import com.example.jetcompose.utils.Loader
import com.example.jetcompose.utils.fontFamilyPR
import com.example.jetcompose.utils.srcImagePath
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
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
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = colorAppBackground,
                    shape = CircleShape,
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
            backgroundColor = colorAppBackground
        )
        {
            val mapDiscover = listViewModel.listDiscover.value
            val listBanner = listViewModel.listTrending.collectAsState()
            val listNowPlaying = listViewModel.listNowPlaying.value
            val listSorted = mapDiscover.keys.sorted().toMutableList()
            listSorted.add(0, "Banner")
            listSorted.add(1, "Now Playing")
            Loader(listNowPlaying.isEmpty())
            LazyColumn {
                items(listSorted) { genreName ->
                    when (genreName) {
                        "Banner" -> Banner(listBanner.value)
                        "Now Playing" -> NowPlaying(genreName, listNowPlaying)
                        else -> Discover(genreName, mapDiscover[genreName]!!)
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun Banner(@PreviewParameter(DiscoverResultsParameterProvider::class) listBanner: List<DiscoverResults>) {
        HorizontalPager(
            itemSpacing = (-40).dp,
            count = listBanner.size,
            contentPadding = PaddingValues(horizontal = 30.dp),
            modifier = Modifier.padding(top = 30.dp, bottom = 10.dp)
        ) { pager ->
                Box {
                    Card(backgroundColor = Color.Transparent,
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
                            .aspectRatio(16 / 9f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Box(Modifier.fillMaxSize()) {
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
                    ItemImage(discoverResults, 150, 230, 12, this@ListFragment)
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
            ItemImage(discoverResults, fragment = this@ListFragment)
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



