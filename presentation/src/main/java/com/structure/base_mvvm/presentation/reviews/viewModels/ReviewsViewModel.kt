package com.structure.base_mvvm.presentation.reviews.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.structure.base_mvvm.domain.search.repository.SearchRepository
import com.structure.base_mvvm.presentation.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
  private val searchRepository: SearchRepository,
  private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
  var test = 0

  init {
    savedStateHandle.get<Int>("teacherId")?.let {
      Log.e("init", ": $it")
      test = it
    }
  }
}