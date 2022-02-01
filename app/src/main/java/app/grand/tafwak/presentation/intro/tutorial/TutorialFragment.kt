package app.grand.tafwak.presentation.intro.tutorial

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.grand.tafwak.domain.intro.entity.AppTutorial
import app.grand.tafwak.appTutorial.AppTutorialHelper
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.auth.AuthActivity
import app.grand.tafwak.presentation.base.BaseFragment
import app.grand.tafwak.presentation.base.extensions.handleApiError
import app.grand.tafwak.presentation.base.extensions.hideKeyboard
import app.grand.tafwak.presentation.base.extensions.openActivityAndClearStack
import com.structure.base_mvvm.databinding.FragmentTutorialBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TutorialFragment : BaseFragment<FragmentTutorialBinding>() {

  private val viewModel: TutorialViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_tutorial

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  private fun setUpAppTutorial(tutorialData: List<AppTutorial> = ArrayList()) {
//
//    tutorialData.apply {
//      tutorialData.add(
//        AppTutorial(
//          "Title - 1",
//          "Some content here for first tutorial screen",
//          getMyDrawable(R.drawable.bg_no_data)
//        )
//      )
//
//      tutorialData.add(
//        AppTutorial(
//          "Title - 2",
//          "Some content here for second tutorial screen",
//          getMyDrawable(R.drawable.bg_no_internet)
//        )
//      )
//
//      tutorialData.add(
//        AppTutorial(
//          "Title - 3",
//          "Some content here for third tutorial screen",
//          getMyDrawable(R.drawable.bg_no_image)
//        )
//      )
//    }

    AppTutorialHelper.Builder(requireActivity(), lifecycle)
      .setTutorialData(tutorialData)
      .setTitleColor(R.color.black)
      .setContentColor(R.color.black_op)
      .setSliderContainerResourceID(R.id.tutorial_container)
      .setActiveIndicatorColor(R.color.colorAccent)
      .setInActiveIndicatorColor(R.color.gray)
      .setAutoScrolling(false)
      .setNextButtonTextColor(R.color.white)
      .setNextButtonBackground(R.color.blue)
      .setNextButtonIcon(R.drawable.ic_back)
      .setPreviousTextColor(R.color.black)
      .setSkipTutorialClick { openIntro() }
      .create()
  }

  override
  fun setupObservers() {
    viewModel.openIntro.observe(this) { openIntro() }
    lifecycleScope.launchWhenResumed {
      viewModel.appTutorialResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            setUpAppTutorial(it.value.data)
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

  private fun openIntro() {
    viewModel.setFirstTime(false)
    openActivityAndClearStack(AuthActivity::class.java)
  }
}