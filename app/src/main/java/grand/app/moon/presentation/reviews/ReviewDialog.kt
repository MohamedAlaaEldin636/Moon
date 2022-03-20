package grand.app.moon.presentation.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import grand.app.moon.domain.auth.enums.AuthFieldsValidation
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.utils.showNoApiErrorAlert
import com.structure.base_mvvm.databinding.ReviewDialogBinding
import grand.app.moon.presentation.reviews.viewModels.ReviewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewDialog : DialogFragment() {
  private val viewModel: ReviewsViewModel by viewModels()
  lateinit var binding: ReviewDialogBinding
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding =
      DataBindingUtil.inflate(inflater, R.layout.review_dialog, container, false)
    binding.viewModel = viewModel
    setupObservers()
    return binding.root
  }

  fun setupObservers() {
    viewModel.validationException.observe(this) {
      when (it) {
        AuthFieldsValidation.EMPTY_CONTENT.value -> {
          showNoApiErrorAlert(requireActivity(), resources.getString(R.string.rate_warning))
        }
      }
    }
    lifecycleScope.launchWhenResumed {
      viewModel.reviewDialogResponse.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            binding.progress.visibility = View.VISIBLE
          }
          is Resource.Success -> {
            binding.progress.visibility = View.GONE
            val bundle = Bundle()
//            bundle.putInt("instructor_id", viewModel.instructorId)
            setFragmentResult(Constants.BUNDLE, bundle)
            dismiss()

          }
          is Resource.Failure -> {
            binding.progress.visibility = View.GONE
            handleApiError(it)
          }
        }
      }
    }
  }
}