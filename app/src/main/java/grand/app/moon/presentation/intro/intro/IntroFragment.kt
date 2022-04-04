package grand.app.moon.presentation.intro.intro

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import coil.load
import grand.app.moon.R
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.databinding.FragmentIntroBinding
import grand.app.moon.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.navigateSafe
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding>() {
  private val introFragmentArgs: IntroFragmentArgs by navArgs()
  private val viewModel: IntroViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_intro

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.positions.set(introFragmentArgs.position)
    if (introFragmentArgs.position == -1) {
      viewModel.callService()
    }else{
      Log.d(TAG, "setBindingVariables: ${introFragmentArgs.position}")
      viewModel.setData((introFragmentArgs.data as BaseResponse<List<AppTutorial>>).data)
      binding.animationView.setAnimationFromUrl(viewModel.data.data[introFragmentArgs.position].image)
      binding.animationView.playAnimation()
    }
  }

  override
  fun setUpViews() {
    setupDots()
  }

  private fun setupDots() {
    when {
      introFragmentArgs.position <= 0 -> {
        binding.imgDots.setImageDrawable(
          ContextCompat.getDrawable(
            requireContext(),
            R.drawable.dot_1
          )
        )
      }
      introFragmentArgs.position == 1 -> {
        binding.imgDots.setImageDrawable(
          ContextCompat.getDrawable(
            requireContext(),
            R.drawable.dot_2
          )
        )
      }
      else -> {
        binding.imgDots.setImageDrawable(
          ContextCompat.getDrawable(
            requireContext(),
            R.drawable.dot_3
          )
        )
      }
    }
  }

  private  val TAG = "IntroFragment"

  override
  fun setupObservers() {
    viewModel.submitEvent.observe(viewLifecycleOwner,{
      when(it){
        Constants.SKIP -> openHome()
        Constants.NEXT -> {
          when(viewModel.isLast.get()){
            true -> openHome()
            else -> {
              navigateSafe(IntroFragmentDirections.actionIntroFragmentSelf(viewModel.data,viewModel.positions.get()!!.plus(1)))
            }
          }
        }
      }
    })
    lifecycleScope.launchWhenResumed {
      viewModel.appTutorialResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            Log.d(TAG, "Set DAta")
            viewModel.setData(it.value.data)
            binding.animationView.setAnimationFromUrl(it.value.data[0].image)
            binding.animationView.playAnimation()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
          else -> {
          }
        }
      }
    }
  }

  private fun openHome() {
    viewModel.setFirstTime()
    openActivityAndClearStack(HomeActivity::class.java)
  }
}