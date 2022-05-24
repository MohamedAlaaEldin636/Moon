package grand.app.moon.presentation.search.views

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentSearchBinding
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.search.viewModels.SearchViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {


  private val viewModel: SearchViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_search

  private val TAG = "SearchFragment"

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
//    setFragmentResultListener(Constants.BUNDLE){ requestKey, bundle ->
//      if(bundle.containsKey(Constants.ID) && bundle.containsKey(Constants.FAVOURITE)) {
//        Log.d(TAG, "onCreate: FAVOURITE")
//        viewModel.adapter.updateFavourite(bundle.getInt(Constants.ID),bundle.getBoolean(Constants.FAVOURITE))
//      }
//    }
  }


  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
//    arguments?.let {
//      viewModel.type = it.getInt("type")
//    }
    if (arguments?.containsKey(Constants.CATEGORY_ID) == true && arguments?.getInt(Constants.CATEGORY_ID) != -1)
      viewModel.filterRequest.categoryId = arguments?.getInt(Constants.CATEGORY_ID)
    if (arguments?.containsKey(Constants.SUB_CATEGORY_ID) == true && arguments?.getInt(Constants.SUB_CATEGORY_ID) != -1)
      viewModel.filterRequest.sub_category_id = arguments?.getInt(Constants.SUB_CATEGORY_ID)

    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
      requireContext(),
      android.R.layout.simple_list_item_1, android.R.id.text1, viewModel.localSearchArrayList
    )
    binding.listView.adapter = adapter
    binding.listView.onItemClickListener =
      AdapterView.OnItemClickListener { parent, view, position, id ->
//        showMessage("welcome $position")
        binding.edtSearch.setText(viewModel.localSearchArrayList[position])
        viewModel.search = viewModel.localSearchArrayList[position]
        viewModel.callService()
      }
  }

  override
  fun setUpViews() {
    binding.edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
      if (i == EditorInfo.IME_ACTION_SEARCH && textView.text.trim().isNotEmpty()) {
        viewModel.reset()
        viewModel.search = textView.text.toString()
        viewModel.callService()
        return@OnEditorActionListener true
      }
      false
    })



    setRecyclerViewScrollListener()
  }


  override fun setupObservers() {

    lifecycleScope.launchWhenResumed {
      viewModel.response.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            viewModel.setData(it.value.data)

          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
        }
        println("collection here $it")
        if (it is Resource.Success) {
          viewModel.setData(it.value.data)
        }
      }

    }
  }


  private fun setRecyclerViewScrollListener() {

    val layoutManager = LinearLayoutManager(requireContext())
    binding.recyclerView.layoutManager = layoutManager
    binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (!recyclerView.canScrollVertically(1)) {
          viewModel.callService()
        }
      }
    })
  }

  override fun onResume() {
    super.onResume()
    viewModel.adapter.updateFavourite()
  }
}