package grand.app.moon.presentation.more

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.databinding.FragmentNotificationBinding
import grand.app.moon.databinding.FragmentSocialBinding
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SwipeToDeleteCallback
import grand.app.moon.presentation.home.viewModels.HomeViewModel
import grand.app.moon.presentation.notfication.viewmodel.NotificationListViewModel
import grand.app.moon.presentation.social.viewModels.SocialViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SocialFragment : BaseFragment<FragmentSocialBinding>() {


  private val viewModel: SocialViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_social

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  override
  fun setUpViews() {

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
	        else -> {}
        }
      }

    }
  }
}