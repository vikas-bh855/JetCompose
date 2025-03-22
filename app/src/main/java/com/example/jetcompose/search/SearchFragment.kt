package com.example.jetcompose.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetcompose.R
import com.example.jetcompose.models.DiscoverResults
import com.example.jetcompose.theme.colorOffWhite
import com.example.jetcompose.utils.ItemImage
import com.example.jetcompose.utils.fontFamilyPR
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false).apply {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(7.dp),
                shape = RoundedCornerShape(10.dp),
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
                ItemImage(
                    discoverResults = searchList.peek(index)!!,
                    fragment = this@SearchFragment
                )
            }
        })

    }
}



