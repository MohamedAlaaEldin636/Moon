package grand.app.moon.presentation.reviews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import grand.app.moon.presentation.reviews.viewModels.ReviewsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.ReviewDialogBinding
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class ReviewDialog : BottomSheetDialogFragment() {
  val args: ReviewDialogArgs by navArgs()
  private val viewModel: ReviewsViewModel by viewModels()
  lateinit var binding: ReviewDialogBinding
  private  val TAG = "ReviewDialog"
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.review_dialog, container, false)
    binding.viewModel = viewModel
    if( args.advertisementId != -1)
      viewModel.request.advertisement_id = args.advertisementId.toString()
    if( args.storeId != -1)
      viewModel.request.store_id = args.storeId.toString()
    Log.d(TAG, "onCreateView: ${viewModel.request.advertisement_id}")
    Log.d(TAG, "onCreateView: ${viewModel.request.store_id}")
    viewModel.rate = args.rate
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
            binding.btnRate.hide()
          }
          is Resource.Success -> {
            binding.progress.visibility = View.GONE
            binding.btnRate.show()
            showMessage(it.value.message)
//            dismiss()
            val n = findNavController()
            n.navigateUp()
            n.currentBackStackEntry?.savedStateHandle?.set(
              Constants.REVIEW,
              true
            )
          }
          is Resource.Failure -> {
            binding.progress.visibility = View.GONE
            handleApiError(it)
            binding.btnRate.show()
          }
        }
      }
    }
  }

  override fun getTheme(): Int {
    return R.style.CustomBottomSheetDialogTheme;
  }

  override fun onDestroy() {
    setFragmentResult(Constants.BUNDLE, bundleOf(Constants.REVIEW to viewModel.reviewAdded)) // total rate
    super.onDestroy()
  }
}