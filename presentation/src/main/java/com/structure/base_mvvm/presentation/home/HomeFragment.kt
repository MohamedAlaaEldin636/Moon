package com.structure.base_mvvm.presentation.home

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import codes.grand.pretty_pop_up.PrettyPopUpHelper
import com.structure.base_mvvm.domain.utils.Constants
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.base.BaseFragment
import com.structure.base_mvvm.presentation.base.extensions.*
import com.structure.base_mvvm.presentation.base.utils.SwipeToDeleteCallback
import com.structure.base_mvvm.presentation.databinding.FragmentHomeBinding
import com.structure.base_mvvm.presentation.home.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

  private val viewModel: HomeViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_home

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    val someKotlinCollectionFunctions = listOf(
      "distinct", "map",
      "isEmpty", "contains",
      "filter", "first",
      "last", "reduce",
      "single", "joinToString"
    )

    val message = someKotlinCollectionFunctions.joinToString(
      separator = ", ",
      prefix = "Kotlin has many collection functions like: ",
      postfix = "and they are awesome.",
      limit = 3
    )
    Log.e("setBindingVariables", "setBindingVariables: $message")
  }

  override
  fun setupObservers() {
    viewModel.clickEvent.observeForever {
      if (it == Constants.TEACHER_PROFILE)
        toTeacherProfile()
    }
    lifecycleScope.launchWhenResumed {
      viewModel.homeResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.homePaginateData = it.value.data
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
      }
    }
    lifecycleScope.launchWhenResumed {
      viewModel.adapter.liveData.collect {
        Log.e("setupObservers", "setupObservers: ")
      }
    }
  }

  private fun toTeacherProfile() {
    navigateSafe(HomeFragmentDirections.actionToTeacherProfileFragment())
  }


}