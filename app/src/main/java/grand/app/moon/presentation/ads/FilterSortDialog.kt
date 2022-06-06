package grand.app.moon.presentation.ads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.databinding.FilterSortDialogBinding
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.presentation.ads.viewModels.FilterDialogViewModel
import grand.app.moon.presentation.base.extensions.backToPreviousScreen
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.HomeFragmentDirections

@AndroidEntryPoint
class FilterSortDialog : BottomSheetDialogFragment() {
  val filter: FilterSortDialogArgs by navArgs()
  private val viewModel: FilterDialogViewModel by viewModels()
  lateinit var binding: FilterSortDialogBinding
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.filter_sort_dialog, container, false)
    binding.viewModel = viewModel
    binding.btnClick.setOnClickListener {
      when (filter.kind) {
        FilterDialog.CHAT ->
          when(viewModel.adapter.lastSelected){
            1 -> findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCommetChatFragment())
            else -> findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWhatsappChatFragment())
          }
        else -> {
          val bundle = Bundle()
          bundle.putInt(Constants.SORT_BY, viewModel.adapter.lastSelected)
          bundle.putString(Constants.TYPE, filter.kind.toString())
          setFragmentResult(Constants.BUNDLE, bundle)
          backToPreviousScreen()
        }
      }
    }
    when (filter.kind) {
      FilterDialog.ADVERTISEMENT -> {
        viewModel.title.set(resources.getString(R.string.sort_by))
        viewModel.list.add(AppTutorial(1, content = resources.getString(R.string.the_most_recent)))
        viewModel.list.add(AppTutorial(2, content = resources.getString(R.string.the_oldest)))
        viewModel.list.add(AppTutorial(3, content = resources.getString(R.string.lowest_price)))
        viewModel.list.add(AppTutorial(4, content = resources.getString(R.string.highest_price)))
      }
      FilterDialog.NOTIFICATION -> {
        viewModel.title.set(resources.getString(R.string.notification_type))
        viewModel.list.add(AppTutorial(3, content = resources.getString(R.string.all)))
        viewModel.list.add(
          AppTutorial(
            1,
            content = resources.getString(R.string.moon_notifications)
          )
        )
        viewModel.list.add(AppTutorial(2, content = resources.getString(R.string.moon_traders)))
      }
      FilterDialog.CHAT -> {
        viewModel.title.set(resources.getString(R.string.conversation))
        viewModel.list.add(AppTutorial(1, content = resources.getString(R.string.chat)))
        viewModel.list.add(AppTutorial(2, content = resources.getString(R.string.whatsapp)))
      }
    }
    viewModel.setData(filter.selected)
    viewModel.adapter.changeEvent.observe(this,{
      viewModel.show.set(true)
    })
    return binding.root
  }

  override fun getTheme(): Int {
    return R.style.CustomBottomSheetDialogTheme;
  }
}