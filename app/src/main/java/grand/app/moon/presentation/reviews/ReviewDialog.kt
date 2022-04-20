package grand.app.moon.presentation.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import app.grand.tafwak.presentation.reviews.viewModels.ReviewsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.ReviewDialogBinding
import grand.app.moon.databinding.WorkingHoursDialogBinding
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.viewModels.WorkingHoursViewModel
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class ReviewDialog : BottomSheetDialogFragment() {
  val args: ReviewDialogArgs by navArgs()
  private val viewModel: ReviewsViewModel by viewModels()
  lateinit var binding: ReviewDialogBinding
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.review_dialog, container, false)
    binding.viewModel = viewModel
//    viewModel.request.advertisement_id = args.advertisementId
    setupObserver()
    return binding.root
  }

  fun setupObserver(){
    lifecycleScope.launchWhenResumed {
      viewModel.reviewDialogResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            binding.progress.visibility = View.VISIBLE
          }
          is Resource.Success -> {
            binding.progress.visibility = View.GONE

          }
          is Resource.Failure -> {
            binding.progress.visibility = View.GONE
            handleApiError(it)
          }
        }
      }
    }
  }

  override fun getTheme(): Int {
    return R.style.CustomBottomSheetDialogTheme;
  }
}