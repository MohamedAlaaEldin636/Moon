package grand.app.moon.presentation.intro.tutorial

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.appTutorial.AppTutorialHelper
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.databinding.FragmentTutorialBinding
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.HomeActivity
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

    AppTutorialHelper.Builder(requireActivity(), lifecycle)
      .setTutorialData(tutorialData)
      .setTitleColor(R.color.colorPrimary)
      .setContentColor(R.color.colorBlack)
      .setSliderContainerResourceID(R.id.tutorial_container)
      .setActiveIndicatorColor(R.color.colorAccent)
      .setInActiveIndicatorColor(R.color.gray)
      .setAutoScrolling(false)
      .setNextButtonTextColor(R.color.white)
      .setNextButtonBackground(R.color.colorPrimary)
      .setNextButtonIcon(R.drawable.ic_back)
      .setPreviousTextColor(R.color.colorPrimary)
      .setSkipTutorialClick { homePage() }
      .setPreviousTutorialClick { homePage() }
      .create()
  }

  override
  fun setupObservers() {

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
//            viewModel.setData(it.value.data)
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
          else -> {}
        }
      }
    }

    viewModel.submitEvent.observe(this) {
      if (it == Constants.SKIP) {
        homePage()
      }
    }
  }

  private fun homePage() {
    viewModel.setFirstTime(false)
    openActivityAndClearStack(HomeActivity::class.java)
  }
}