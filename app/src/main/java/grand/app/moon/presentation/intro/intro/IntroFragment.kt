package grand.app.moon.presentation.intro.intro

import androidx.fragment.app.viewModels
import grand.app.moon.R
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.databinding.FragmentIntroBinding
import grand.app.moon.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding>() {

  private val viewModel: IntroViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_intro

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  override
  fun setUpViews() {
  }


  override
  fun setupObservers() {
    viewModel.openLogIn.observe(this) {
      viewModel.setFirstTime(false)
      openLogIn()
    }
  }

  private fun openLogIn() {
    openActivityAndClearStack(AuthActivity::class.java)
  }

  private fun openHome() {
    openActivityAndClearStack(HomeActivity::class.java)
  }
}