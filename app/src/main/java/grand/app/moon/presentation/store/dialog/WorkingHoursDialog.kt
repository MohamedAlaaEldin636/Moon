package grand.app.moon.presentation.store.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.WorkingHoursDialogBinding
import grand.app.moon.presentation.store.viewModels.StoreDetailsViewModel
import grand.app.moon.presentation.store.viewModels.WorkingHoursViewModel
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.concurrent.schedule

@AndroidEntryPoint
class WorkingHoursDialog : BottomSheetDialogFragment() {
  val workingHoursDialogArgs: WorkingHoursDialogArgs by navArgs()
  private val viewModel: WorkingHoursViewModel by viewModels()
  lateinit var binding: WorkingHoursDialogBinding
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.working_hours_dialog, container, false)
    binding.viewModel = viewModel
    viewModel.adapter.differ.submitList(workingHoursDialogArgs.store.workingHours)
    binding.btnBack.setOnClickListener {
      dismiss()
    }
    return binding.root
  }

  override fun getTheme(): Int {
    return R.style.CustomBottomSheetDialogTheme;
  }
}