package grand.app.moon.presentation.filter.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FilterSingleSelectDialogBinding
import grand.app.moon.presentation.base.extensions.backToPreviousScreen
import grand.app.moon.presentation.base.utils.Constants

@AndroidEntryPoint
class FilterSingleSelectDialog : DialogFragment() {
  val args: FilterSingleSelectDialogArgs by navArgs()
  private val viewModel: FilterChildrenDialogViewModel by viewModels()
  private  val TAG = "FilterSingleSelectDialo"
  lateinit var binding: FilterSingleSelectDialogBinding
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.filter_single_select_dialog, container, false)
    binding.viewModel = viewModel
    binding.btnClick.setOnClickListener {
      val bundle = Bundle()
      if(viewModel.adapter.lastPosition != -1) {
        viewModel.property.selectedList.clear()
        viewModel.property.selectedList.add(viewModel.adapter.differ.currentList[viewModel.adapter.lastPosition].id)
        viewModel.property.selectedText =
          viewModel.adapter.differ.currentList[viewModel.adapter.lastPosition].content.toString()
//        +"("+viewModel.adapter.differ.currentList[viewModel.adapter.lastPosition].id+")"
//        Log.d(TAG, "Select: ${viewModel.property.selectedText}")
      }
      bundle.putSerializable(Constants.PROPERTY,viewModel.property)
      setFragmentResult(Constants.BUNDLE, bundle)
      backToPreviousScreen()
    }
    viewModel.setData(args.propety)
    if(viewModel.adapter.lastPosition != -1){
      viewModel.show.set(true)
    }

    viewModel.adapter.changeEvent.observe(this,{
      viewModel.show.set(true)
    })
//    args.lastSelectId?.let {
//    }
    return binding.root
  }
  override fun getTheme(): Int {
    return R.style.CustomDialogFullScreen;
  }
}