package grand.app.moon.presentation.packages.viewModel

import android.app.Application
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.databinding.ItemPageIndicatorBinding
import grand.app.moon.domain.packages.BasePagination
import grand.app.moon.domain.packages.ResponsePackage
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.RVItemCommonListUsage
import grand.app.moon.extensions.toJsonInlinedOrNull
import grand.app.moon.presentation.packages.PageBecomeShopPackageFragment
import javax.inject.Inject

@HiltViewModel
class BecomeShopPackagesViewModel @Inject constructor(
	application: Application,
	val repoPackages: RepositoryPackages,
) : AndroidViewModel(application) {

	var currentlySelectedPageIndex = 0

	var currentResponsePagination: BasePagination<ResponsePackage>? = null

	var allPackages = emptyList<ResponsePackage>()

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

	private var pagerAdapter: PagerAdapter? = null

	@Synchronized
	fun getPagerAdapter(fragment: Fragment): PagerAdapter {
		return pagerAdapter ?: PagerAdapter(fragment).also {
			pagerAdapter = it
		}
	}

	fun changePageIndicatorsSize(size: Int) {
		adapter.submitList(List(size) { it })
	}

	fun changeSelectedPageIndicator(index: Int) {
		currentlySelectedPageIndex = index
		adapter.notifyDataSetChanged()
	}

	override fun onCleared() {
		pagerAdapter = null

		super.onCleared()
	}

	inner class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
		override fun getItemCount(): Int = allPackages.size

		override fun createFragment(position: Int): Fragment = PageBecomeShopPackageFragment().also {
			MyLogger.e("euiwhid -> ch 0 -> ${allPackages.getOrNull(position).toJsonInlinedOrNull()}")

			it.arguments = bundleOf(
				PageBecomeShopPackageFragment.ARGS_ITEM to allPackages.getOrNull(position).toJsonInlinedOrNull()
			)
		}
	}

}
