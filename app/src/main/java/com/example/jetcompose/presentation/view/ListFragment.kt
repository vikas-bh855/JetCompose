package com.example.jetcompose.presentation.view

import android.animation.ObjectAnimator
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.jetcompose.data.models.Genres
import com.example.jetcompose.databinding.ListFragmentBinding
import com.example.jetcompose.fontFamilyPR
import com.example.jetcompose.presentation.viewmodel.ListViewModel
import com.example.jetcompose.srcImagePath
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


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
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .verticalScroll(enabled = true, state = ScrollState(0))
                    ) {
                        val genreList = listViewModel.listGenres.collectAsState()
                        val listDiscover = listViewModel.listDiscover.collectAsState(
                            emptyList()
                        )
                        GenreList(genreList, listDiscover)
                        GenreTile(genreList)
                    }
                }
            }
        }
    }

    @Composable
    fun GenreList(items: State<List<Genres>>?, listDiscover: State<List<DiscoverResults>>) {
        items?.let {
            items.value.forEachIndexed { index, genres ->
                if (index == 1)
                    Genres(genre = "By Popularity", genres.id, listDiscover)
            }
        }
    }

    @Composable
    fun GenreTile(genreList: State<List<Genres>>) {
        ShowTonalButton(title = "By Genres")
        LazyRow(Modifier.padding(vertical = 10.dp)) {
            itemsIndexed(genreList.value) { index, genres ->
                Card(
                    Modifier
                        .padding(end = 10.dp)
                        .size(100.dp, 70.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    val colors1 = listOf(Color(0xFF31C7D0), Color(0xFF67A55D))
                    val colors2 = listOf(Color(0xFFAFA1D6), Color(0xFF2E47C7))
                    val colors3 = listOf(Color(0xFFDBC1C1), Color(0xFFF44040))
                    var colors =
                        if (index % 2 == 0) colors1 else colors2
                    colors = if ((index + 1) % 3 == 0) colors3 else colors
                    Box(
                        Modifier.background(brush = Brush.linearGradient(colors)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = genres.name,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = fontFamilyPR,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun Genres(
        genre: String = "Genre Loading...",
        id: String = "",
        listDiscover: State<List<DiscoverResults>>
    ) {
        listViewModel.getDiscoverGenres(id)
        ShowTonalButton(genre)
        DiscoverItem(listDiscover.value)
    }

    @Composable
    fun ShowTonalButton(title: String) {
        FilledTonalButton(
            contentPadding = PaddingValues(horizontal = 15.dp),
            modifier = Modifier.padding(vertical = 5.dp),
            onClick = {},
            elevation = ButtonDefaults.buttonElevation(10.dp),
            shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = Color(0xffFAC682)
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
    }


    @Composable
    fun DiscoverItem(listDiscover: List<DiscoverResults> = emptyList()) {
        if (listDiscover.isNotEmpty()) {
            Log.d(TAG, "DiscoverItem: ")
            LazyRow(Modifier.padding(vertical = 10.dp)) {
                items(listDiscover) { discover ->
                    ShowImage(discover, 120, 180)
                }
            }
        }
    }

    @Composable
    fun ShowImage(
        discoverResults: DiscoverResults,
        width: Int, height: Int, isBanner: Boolean = false
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

