package com.example.jetcompose.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.example.jetcompose.R
import com.example.jetcompose.data.models.DiscoverResults
import com.example.jetcompose.fontFamilyPR
import com.example.jetcompose.srcImagePath
import com.example.jetcompose.ui.theme.colorOffWhite
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).apply {
            duration = 400
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
                SearchBar()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SearchBar() {
        val searchString = remember { mutableStateOf("") }
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
        ) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(7.dp),
                shape = RoundedCornerShape(10.dp),
                backgroundColor = Color(0xFF23232E)
            ) {
                ConstraintLayout(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp)
                ) {
                    val (editField, searchImg) = createRefs()
                    val text = remember { mutableStateOf("") }
                    BasicTextField(
                        modifier = Modifier.constrainAs(editField) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                        value = text.value,
                        onValueChange = {
                            text.value = it
                            if (it.length > 3) {
                                searchString.value = it
                            }
                        },
                        decorationBox = { innerTextField ->
                            if (text.value.isEmpty()) {
                                Text("Search", color = Color(0XFF53565B))
                            }
                            innerTextField()  //<-- Add this
                        },
                        textStyle = TextStyle(fontFamily = fontFamilyPR, color = colorOffWhite)
                    )
                    Image(
                        modifier = Modifier
                            .size(20.dp)
                            .constrainAs(searchImg) {
                                top.linkTo(editField.top)
                                bottom.linkTo(editField.bottom)
                                end.linkTo(editField.end)
                            },
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "search"
                    )
                }
            }
            if (searchString.value.isNotBlank())
                SearchGrid(searchViewModel.getSearch(searchString.value).collectAsLazyPagingItems())
        }
    }

    @Composable
    fun SearchGrid(searchList: LazyPagingItems<DiscoverResults>) {
        LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
            items(searchList.itemCount) { index ->
                Image(discoverResults = searchList.peek(index)!!)
            }
        })

    }


    @Composable
    fun Image(
        discoverResults: DiscoverResults,
    ) {
        BoxWithConstraints {
            Image(
                painter = rememberImagePainter(data = discoverResults.poster_path.srcImagePath,
                    builder = {
                        crossfade(true)
                    }),
                contentDescription = discoverResults.title,
                modifier = Modifier
                    .clickable {
                        val directions =
                            SearchFragmentDirections.actionSearchFragmentToActionDetailFragment(
                                discoverResults
                            )
                        findNavController().navigate(directions)
                    }
                    .height(maxWidth * 16 / 11)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit,
            )
        }
    }
}



