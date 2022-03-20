package com.example.jetcompose.presentation.view

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.FabPosition
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.load
import coil.request.ImageRequest
import com.example.jetcompose.R
import com.example.jetcompose.data.models.DiscoverResults
import com.example.jetcompose.data.models.DiscoverResultsParameterProvider
import com.example.jetcompose.databinding.ListFragmentBinding
import com.example.jetcompose.fontFamilyPR
import com.example.jetcompose.presentation.viewmodel.ListViewModel
import com.example.jetcompose.srcImagePath
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@AndroidEntryPoint
class ListFragment : Fragment() {
    private val TAG = "ListFragment"
    private val listViewModel by viewModels<ListViewModel>()
    private lateinit var binding: ListFragmentBinding
    private var currentPos = -1

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backImg.setRenderEffect(RenderEffect.createBlurEffect(40f, 40f, Shader.TileMode.CLAMP))
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    listViewModel.listPopular.collect {
                        if (it.isNotEmpty())
                            backImg.load(it.first().poster_path.srcImagePath)
                        carouselAdapter.setAdapter(object : Carousel.Adapter {
                            override fun count(): Int = it.size

                            override fun populate(view: View?, index: Int) {
                                currentPos = index
                                (view as ImageView).load(it[index].backdrop_path.srcImagePath)
                            }

                            override fun onNewItem(index: Int) {
                                val imageLoader = ImageLoader(requireContext())
                                val request = ImageRequest.Builder(requireContext())
                                        .data(it[index].poster_path.srcImagePath)
                                        .target {
                                            ObjectAnimator.ofFloat(backImg, View.ALPHA, 0.2f, 0.4f)
                                                    .setDuration(500)
                                                    .start()
                                            backImg.setImageDrawable(it)
                                        }
                                        .build()
                                imageLoader.enqueue(request)
                            }
                        })
                        if (it.isNotEmpty())
                            carouselAdapter.refresh()
                    }
                }
            }
            binding.composeView.apply {
                setContent {
                    DiscoverList()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview()
    @Composable
    fun DiscoverList() {
        var isClick by remember { mutableStateOf(false) }
        val translationAnimation by animateFloatAsState(
                targetValue = if (isClick) 150f else 0f,
                animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = Spring.StiffnessMediumLow
                )
        )
        val zIndexAnim by animateFloatAsState(targetValue = if (isClick) 30f else 0f,
                animationSpec = keyframes {
                    durationMillis = 750
                    0f at 500 with LinearEasing
                    30f at 750 with FastOutSlowInEasing
                })
        Scaffold(
                bottomBar = {
                    BottomAppBar(
                            cutoutShape = CircleShape,
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                            backgroundColor = Color.Transparent,
                            elevation = 0.dp,
                            contentPadding = PaddingValues(0.dp)
                    ) {
                        var selectedItem by remember { mutableStateOf("") }
                        Card(Modifier.fillMaxSize(), shape = RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp), backgroundColor = Color(0xFFFDF8F2)) {
                            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                                BottomNavigationItem(selected = false,
                                        onClick = {
                                            selectedItem = "profile"
                                            startActivity(Intent(requireActivity(), ProfileActivity::class.java))
                                        },
                                        icon = {
                                            if (selectedItem == "profile")
                                                Icon(Icons.Filled.AccountCircle, "", Modifier.size(25.dp))
                                            else Icon(Icons.Outlined.AccountCircle, "", Modifier.size(25.dp))
                                        })
                                BottomNavigationItem(selected = true,
                                        onClick = { selectedItem = "settings" },
                                        icon = {
                                            if (selectedItem == "settings")
                                                Icon(Icons.Filled.Settings, "", Modifier.size(25.dp))
                                            else Icon(Icons.Outlined.Settings, "", Modifier.size(25.dp))
                                        })
                            }

                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                            shape = CircleShape,
                            onClick = {},
                            modifier = Modifier
                                    .graphicsLayer {
                                        translationX = -translationAnimation
                                        translationY = -translationAnimation
                                    }
                                    .zIndex(zIndexAnim)) {
                    }
                    FloatingActionButton(
                            shape = CircleShape,
                            onClick = {},
                            modifier = Modifier
                                    .graphicsLayer {
                                        translationX = translationAnimation
                                        translationY = -translationAnimation
                                    }
                                    .zIndex(zIndexAnim)) {
                    }

                    FloatingActionButton(
                            shape = CircleShape,
                            onClick = {},
                            modifier = Modifier
                                    .graphicsLayer {
                                        translationY = -translationAnimation.times(2)
                                    }
                                    .zIndex(zIndexAnim)) {
                    }
                    FloatingActionButton(shape = CircleShape, onClick = { isClick = !isClick }, modifier = Modifier.zIndex(10f)) {
                    }
                },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center,
                backgroundColor = Color.Transparent
        )
        {
            val list = listViewModel.listDiscover.value
            LazyColumn {
                items(list.keys.toList()) { genreName ->
                    DiscoverItem(genreName, list[genreName]!!)
                }
            }
        }
    }

    @Preview
    @Composable
    fun DiscoverItem(title: String = "title", listDiscover: List<DiscoverResults> = emptyList()) {
        if (listDiscover.isNotEmpty()) {
            FilledTonalButton(
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = {},
                    elevation = ButtonDefaults.buttonElevation(10.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0xFFFFCCBC)
                    )
            ) {
                Text(
                        text = title,
                        color = Color(0xff1D2729),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyPR
                )
            }
            LazyRow(Modifier.padding(vertical = 10.dp)) {
                items(listDiscover) { discover ->
                    ShowImage(discover, 120, 180)
                }
            }
        }
    }

    @Preview
    @Composable
    fun ShowImage(
            @PreviewParameter(DiscoverResultsParameterProvider::class)
            discoverResults: DiscoverResults,
            width: Int = 120, height: Int = 180, isBanner: Boolean = false
    ) {
        Column(
                modifier = Modifier
                        .width(width.dp)
                        .height(210.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                            .size(width.dp, height.dp)
                            .padding(5.dp)
                            .clickable(onClick = {
                                val directions = ListFragmentDirections.actionListFragmentToDetailFragment(
                                        discoverResults
                                )
                                findNavController().navigate(directions)
                            }),
                    elevation = 10.dp
            ) {
                val srcPath = when (isBanner) {
                    true -> discoverResults.backdrop_path
                    else -> discoverResults.poster_path
                }
                Image(
                        painter = rememberImagePainter(srcPath.srcImagePath),
                        contentDescription = discoverResults.title,
                        modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                )
            }
            Text(
                    text = discoverResults.title,
                    fontFamily = FontFamily(Font(R.font.poppinsr)),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
            )
        }
    }
}


