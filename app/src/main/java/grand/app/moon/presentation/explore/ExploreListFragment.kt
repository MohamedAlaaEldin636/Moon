package grand.app.moon.presentation.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentExploreListBinding
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.domain.home.models.Store
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.explore.viewmodel.ExploreListViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ExploreListFragment : BaseFragment<FragmentExploreListBinding>() {

  val viewModel: ExploreListViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_explore_list

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel

    if(arguments != null && arguments?.containsKey("list") == true && arguments?.getString("list")?.isNotEmpty() == true){
      val exploreListPaginateData = ExploreListPaginateData()
      val gson = GsonBuilder().create()
      val list = gson.fromJson<ArrayList<Explore>>(requireArguments().getString("list"), object :
        TypeToken<ArrayList<Explore>>() {}.type)
      exploreListPaginateData.list.addAll(list)
      viewModel.adapter.fromStore = true
      viewModel.setData(exploreListPaginateData)
    }else {
      val exploreListFragmentArgs: ExploreListFragmentArgs by navArgs()
      viewModel.page = exploreListFragmentArgs.page
      exploreListFragmentArgs.data.list.forEachIndexed { index, item ->
        Log.d(TAG, "onStart: $index , ${item.id} , ${item.likes}")
      }
      viewModel.setData(exploreListFragmentArgs.data)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Log.d(TAG, "onViewCreated: HERE")
    setFragmentResultListener(Constants.BUNDLE) { requestKey, bundle ->
      Log.d(TAG, "onViewCreated: BUNDLE")
      // We use a String here, but any type that can be put in a Bundle is supported
      if(bundle.containsKey(Constants.TOTAL)) {
        val result = bundle.getInt(Constants.TOTAL)
        Log.d(TAG, "onViewCreated: ${result}")
        viewModel.adapter.updateTotalComments(bundle.getInt(Constants.POSITION),result)
      }
      // Do something with the result
    }
  }
  override
  fun setUpViews() {
  }

  private val TAG = "ExploreListFragment"

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
	        else -> {}
        }
      }
    }

    viewModel.clickEvent.observe(this, {
      if (it == Constants.LOGIN_REQUIRED) openLoginActivity()
    })

  }
  fun share(){
    viewModel.adapter.differ.currentList[viewModel.adapter.position].shares++
    viewModel.adapter.notifyItemChanged(viewModel.adapter.position)
    context?.let { it1 -> share(it1,resources.getString(R.string.app_name),resources.getString(R.string.moon_info)+"\n"+viewModel.adapter.differ.currentList[viewModel.adapter.position].file) }


  }

  override fun onResume() {
    super.onResume()
    viewModel.adapter.checkFollowingStore()
    viewModel.adapter.checkBlockStore()
    viewModel.adapter.user = viewModel.userLocalUseCase.invoke()
    viewModel.adapter.notifyDataSetChanged()
  }

  override fun onDestroy() {
    Log.d(TAG, "onDestroy: EXPLORE LIST")
    val bundle = Bundle()
    val paginateData = ExploreListPaginateData()
    val list = ArrayList(viewModel.adapter.differ.currentList)
    list.forEachIndexed{ index , item ->
      Log.d(TAG, "onDestroy: $index , ${item.id} , ${item.likes}")
    }
    paginateData.list.addAll(list)
    bundle.putSerializable(Constants.EXPLORES,paginateData)
    setFragmentResult(Constants.BUNDLE, bundle)
    super.onDestroy()
  }
}