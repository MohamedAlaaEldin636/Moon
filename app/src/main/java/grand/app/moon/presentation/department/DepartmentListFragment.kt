package grand.app.moon.presentation.department

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentDepartmentBinding
import grand.app.moon.presentation.department.viewmodel.DepartmentListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DepartmentListFragment : BaseFragment<FragmentDepartmentBinding>() {

  private val viewModel: DepartmentListViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_department

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.callService()
  }

  override
  fun setUpViews() {
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
        if(it is Resource.Success){
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
        if (!recyclerView.canScrollVertically(1)){
          viewModel.callService()
        }
      }
    })
  }
}