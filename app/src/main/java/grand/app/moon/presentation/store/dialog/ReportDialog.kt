package grand.app.moon.presentation.store.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.ReportDialogBinding
import grand.app.moon.databinding.WorkingHoursDialogBinding
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.extensions.handleApiError
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.store.viewModels.ReportViewModel
import grand.app.moon.presentation.store.viewModels.StoreDetailsViewModel
import grand.app.moon.presentation.store.viewModels.WorkingHoursViewModel
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.concurrent.schedule

@AndroidEntryPoint
class ReportDialog : BottomSheetDialogFragment() {
  val reportArgs: ReportDialogArgs by navArgs()
  private val viewModel: ReportViewModel by viewModels()
  lateinit var binding: ReportDialogBinding
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.report_dialog, container, false)
    binding.viewModel = viewModel
    viewModel.title.set(reportArgs.title)
    setupObserver()
    viewModel.callService()
    return binding.root
  }

  fun setupObserver(){
    lifecycleScope.launchWhenResumed {
      viewModel.response.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            viewModel.progress.set(true)
          }
          is Resource.Success -> {
            viewModel.progress.set(false)
            viewModel.setData(it.value.data)
          }
          is Resource.Failure -> {
            viewModel.progress.set(false)
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