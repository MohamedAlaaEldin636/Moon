package grand.app.moon.presentation.filter.dialog.multiCheck

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FilterMultiSelectDialogBinding
import grand.app.moon.presentation.base.extensions.backToPreviousScreen
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.filter.dialog.FilterChildrenDialogViewModel

@AndroidEntryPoint
class FilterMultiSelectDialog : BottomSheetDialogFragment() {
  val args: FilterMultiSelectDialogArgs by navArgs()
  private val viewModel: FilterMultiSelectDialogViewModel by viewModels()
  lateinit var binding: FilterMultiSelectDialogBinding
  private  val TAG = "FilterMultiSelectDialog"
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding =
      DataBindingUtil.inflate(inflater, R.layout.filter_multi_select_dialog, container, false)
    binding.viewModel = viewModel
    binding.btnClick.setOnClickListener {
      val bundle = Bundle()
      viewModel.property.selectedList.clear()
      var selectedList = ArrayList<String>()
      viewModel.adapter.differ.currentList.forEach { model ->
        if(viewModel.adapter.selected.contains(model.id))
          selectedList.add(model.content!!)
      }
      viewModel.property.selectedList.addAll(viewModel.adapter.selected)
      viewModel.property.selectedText = selectedList.joinToString(
        prefix = "",
        separator = "-",
        postfix = "",
        limit = 4,
        truncated = "..."
      )
      bundle.putSerializable(Constants.PROPERTY, viewModel.property)
      setFragmentResult(Constants.BUNDLE, bundle)
      backToPreviousScreen()
    }
    Log.d(TAG, "setData: ${args.propety.children.size}")

    viewModel.setData(args.propety)
    return binding.root
  }

  override fun getTheme(): Int {
    return R.style.CustomBottomSheetDialogTheme;
  }
}