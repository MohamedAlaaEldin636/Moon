package grand.app.moon.presentation.packages.viewModel

import android.app.Application
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.databinding.ItemPageIndicatorBinding
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.packages.BasePagination
import grand.app.moon.domain.packages.ResponseBecomeShopPackage
import grand.app.moon.extensions.RVItemCommonListUsage
import javax.inject.Inject

@HiltViewModel
class BecomeShopPackagesViewModel @Inject constructor(
	application: Application,
	val repoPackages: RepositoryPackages,
) : AndroidViewModel(application) {

	var currentlySelectedPageIndex = 0

	var currentResponsePagination: BasePagination<List<ResponseBecomeShopPackage>>? = null

	var allPackages = emptyList<ResponseBecomeShopPackage>()

	val adapter = RVItemCommonListUsage<ItemPageIndicatorBinding, Int>(
		R.layout.item_page_indicator,
	) { binding, position, _ ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		val colorRes = if (position == currentlySelectedPageIndex) {
			R.color.selected_page_indicator
		}else {
			R.color.unselected_page_indicator
		}

		binding.view.backgroundTintList = ColorStateList.valueOf(
			ContextCompat.getColor(context, colorRes)
		)
	}

	fun changePageIndicatorsSize(size: Int) {
		adapter.submitList(List(size) { it })
	}

	fun changeSelectedPageIndicator(index: Int) {
		currentlySelectedPageIndex = index
		adapter.notifyDataSetChanged()
	}

}
