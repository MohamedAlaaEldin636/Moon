package grand.app.moon.presentation.map.viewModel

import android.app.Application
import android.content.res.ColorStateList
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreCategoryInMyAdsBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.extensions.*
import grand.app.moon.presentation.map.MapOfDataFragmentArgs
import grand.app.moon.presentation.map.model.MAClusterItem
import grand.app.moon.presentation.map.model.ResponseMapData
import javax.inject.Inject

@HiltViewModel
class MapOfDataViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: MapOfDataFragmentArgs,
	val userLocalUseCase: UserLocalUseCase,
) : AndroidViewModel(application) {

	var myCurrentLocation: LatLng? = null

	/** [Map.Entry.key] represents id */
	var bitmapsDataMap = emptyMap<Int, Bitmap>()
	/** [Map.Entry.key] represents count */
	var bitmapsClustersMap = emptyMap<Int, Bitmap>()

	var googleMap: GoogleMap? = null

	var clusterManager: ClusterManager<MAClusterItem>? = null

	private val allCategories = repoShop.getCategoriesWithSubCategoriesAndBrands()

	val selectedCategory = MutableLiveData<ItemCategory?>(
		allCategories.firstOrNull { it.id == args.categoryId }
	)

	var allDataList: List<ResponseMapData>? = null

	val adapterCategories by lazy {
		RVItemCommonListUsage<ItemStoreCategoryInMyAdsBinding, ItemCategory>(
			R.layout.item_store_category_in_my_ads,
			listOf(ItemCategory.createAllItem(app)).plus(
				repoShop.getCategoriesWithSubCategoriesAndBrands()
			),
			onItemClick = { adapter, binding ->
				val id = binding.textView.tag as? Int

				val oldSelectionPosition = allCategories.indexOfFirstOrNull {
					it.id == selectedCategory.value?.id
				}?.inc().orZero()
				val newSelectionPosition = allCategories.indexOfFirstOrNull {
					it.id == id
				}?.inc().orZero()

				if (oldSelectionPosition == newSelectionPosition) return@RVItemCommonListUsage

				selectedCategory.value = allCategories.firstOrNull { it.id == id }

				adapter.notifyItemsChanged(oldSelectionPosition, newSelectionPosition)
			}
		) { binding, position, item ->
			binding.textView.tag = item.id

			binding.textView.text = item.name

			val isSelected = (position == 0 && selectedCategory.value == null)
				|| selectedCategory.value?.id == item.id
			binding.textView.backgroundTintList = ColorStateList.valueOf(
				ContextCompat.getColor(app, if (isSelected) R.color.star_enabled else R.color.colorPrimary)
			)
		}
	}

	fun getFilteredDataList(): List<ResponseMapData> {
		return allDataList.orEmpty().let { list ->
			val selectedCategoryId = selectedCategory.value?.id

			if (selectedCategoryId == null) list else list.filter { response ->
				response.categories?.any { it.id == selectedCategoryId }.orFalse()
			}
		}
	}

}
