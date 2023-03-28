package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.os.Build
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemHomeRvCategoryBinding
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.CategoryDetails2Fragment
import grand.app.moon.presentation.map.MapOfDataFragment
import javax.inject.Inject

@HiltViewModel
class AllCategoriesViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	state: SavedStateHandle
) : AndroidViewModel(application) {

	companion object {
		private const val KEY_STATE_SEARCH_QUERY = "KEY_STATE_SEARCH_QUERY"
	}

	private val allCategories = repoShop.getCategoriesWithSubCategoriesAndBrands()

	val searchQuery = state.getLiveData(KEY_STATE_SEARCH_QUERY, "")

	val filteredCategories = searchQuery.map { searchQuery ->
		allCategories.filter { searchQuery.orEmpty() in it.name.orEmpty() }
	}

	val adapter = RVItemCommonListUsage<ItemHomeRvCategoryBinding, ItemCategory>(
		R.layout.item_home_rv_category,
		onItemClick = { _, binding ->
			val item = (binding.root.tag as? String).fromJsonInlinedOrNull<ItemCategory>()
				?: return@RVItemCommonListUsage

			CategoryDetails2Fragment.launch(
				binding.root.findNavController(),
				item.id.orZero(),
				item.name.orEmpty()
			)
		}
	) { binding, _, item ->
		binding.root.tag = item.toJsonInlinedOrNull()

		binding.textTextView.text = item.name

		binding.imageImageView.setupWithGlide {
			load(item.image).saveDiskCacheStrategyAll()
		}

		/*binding.textTextView.isSingleLine = true
		binding.textTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
		binding.textTextView.marqueeRepeatLimit = -1
		binding.textTextView.isFocusable = true
		binding.textTextView.isFocusableInTouchMode = true
		binding.textTextView.setHorizontallyScrolling(true)
		binding.textTextView.isSelected = true*/
	}


	fun goToMap(view: View) {
		MapOfDataFragment.goToThisScreenForStores(view.findNavController())
	}

}
