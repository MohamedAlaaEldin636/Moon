package grand.app.moon.presentation.story.view

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentAdsDetailsBinding
import grand.app.moon.databinding.FragmentAdsListBinding
import grand.app.moon.databinding.FragmentStoriesListBinding
import grand.app.moon.presentation.ads.viewModels.AdsDetailsViewModel
import grand.app.moon.presentation.ads.viewModels.AdsListViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.viewModels.HomeViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StoriesListFragment : BaseFragment<FragmentStoriesListBinding>() {

  private val viewModel: HomeViewModel by viewModels()

  val args : StoriesListFragmentArgs by navArgs()

  override
  fun getLayoutId() = R.layout.fragment_stories_list

  private  val TAG = "AdsListFragment"

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    Log.d(TAG, "setBindingVariables: ${args.stories.list.size}")
    viewModel.storiesAllAdapter.differ.submitList(args.stories.list)
  }

  override
  fun setUpViews() {
    setRecyclerViewScrollListener()
  }


  override fun setupObservers() {

  }


  private fun setRecyclerViewScrollListener() {
  }
}