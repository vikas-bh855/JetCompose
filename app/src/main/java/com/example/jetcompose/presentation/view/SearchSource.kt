package com.example.jetcompose.presentation.view

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jetcompose.data.models.Discover
import com.example.jetcompose.data.models.DiscoverResults
import retrofit2.HttpException
import java.io.IOException


class SearchSource constructor(private val searchText: String, private val searchRepository: SearchRepository) :
    PagingSource<Int, DiscoverResults>() {

    override fun getRefreshKey(state: PagingState<Int, DiscoverResults>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DiscoverResults> {
        return try {
            val nextPage = params.key ?: 1
            val searchList = searchRepository.fetchSearch(searchText, page = nextPage)
            if (searchList is com.example.jetcompose.data.source.Result.Success<*>) {
                LoadResult.Page(
                    data = (searchList.data as Discover).results,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = if (searchList.data.results.isEmpty()) null else (searchList.data.page + 1)
                )
            } else {
                LoadResult.Error(RuntimeException(""))
            }

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
