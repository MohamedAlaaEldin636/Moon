package grand.app.moon.presentation.map.viewModel

import android.app.Application
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreCategoryInMyAdsBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.map.MapOfDataFragmentArgs
import grand.app.moon.presentation.map.model.MAClusterItem
import grand.app.moon.presentation.map.model.ResponseMapData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapOfDataViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: MapOfDataFragmentArgs,
	val userLocalUseCase: UserLocalUseCase,
) : AndroidViewModel(application) {

	val selectedMapData = MutableLiveData<ResponseMapData?>()

	val title = selectedMapData.map {
		it?.title.orEmpty()
	}
	val price = selectedMapData.map {
		"${it?.price.orZero()} ${it?.country?.currency.orEmpty()}"
	}
	val image = selectedMapData.map {
		it?.image.orEmpty()
	}
	val showFav = selectedMapData.mapToMutableLiveData {
		it?.isFavorite.orFalse()
	}
	val showSale = selectedMapData.map {
		it?.price.orZero() < it?.priceBefore.orZero()
	}
	val showNegotiable = selectedMapData.map {
		it?.isNegotiable.orFalse()
	}
	val rating = selectedMapData.map { item ->
		"( ${item?.averageRate?.round(1).orZero()
			.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"
	}
	val favsCountText = selectedMapData.map { item ->
		item?.favoriteCount.orZero().toStringOrEmpty()
	}
	val viewsCountText = selectedMapData.map { item ->
		item?.viewsCount.orZero().toStringOrEmpty()
	}
	val timeText = selectedMapData.map { item ->
		item?.createdAt.orEmpty()
	}
	val placeText = selectedMapData.map { item ->
		"${item?.country?.name.orEmpty()} / ${item?.city?.name.orEmpty()}"
	}
	fun goToAdvDetails(view: View) {
		val item = selectedMapData.value ?: return

		userLocalUseCase.goToAdvDetailsCheckIfMyAdv(
			view.context ?: return,
			view.findNavController(),
			item
		)
	}
	fun launchWhatsApp(view: View) {
		val phone = "${selectedMapData.value?.country?.countryCode.orEmpty()}${selectedMapData.value?.phone.orEmpty()}"

		val context = view.context ?: return

		context.applicationScope?.launch {
			repoShop.interactionForAdWhatsApp(selectedMapData.value?.id.orZero())
		}

		view.context?.launchWhatsApp(phone)
	}
	fun launchCall(view: View) {
		val phone = "${selectedMapData.value?.country?.countryCode.orEmpty()}${selectedMapData.value?.phone.orEmpty()}"

		val context = view.context ?: return

		context.applicationScope?.launch {
			repoShop.interactionForAdCall(selectedMapData.value?.id.orZero())
		}

		view.context?.launchDialNumber(phone)
	}
	fun launchChat(view: View) {
		val context = view.context ?: return

		if (context.isLoginWithOpenAuth()) {
			context.applicationScope?.launch {
				repoShop.interactionForAdChat(selectedMapData.value?.id.orZero())
			}

			selectedMapData.value?.also {
				context.openChatStore(view, it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
			}
		}
	}
	fun toggleFavorite(view: View) {
		val context = view.context ?: return
		val item = selectedMapData.value ?: return

		if (context.isLoginWithOpenAuth()) {
			val newIsFav = item.isFavorite.orFalse().not()
			item.isFavorite = newIsFav
			allDataList?.firstOrNull { it.id == item.id }?.isFavorite = newIsFav
			showFav.value = newIsFav
			context.applicationScope?.launch {
				repoShop.favOrUnFavAdv(item.id.orZero())
			}
		}
	}

	var myCurrentLocation: LatLng? = null

	/** [Map.Entry.key] represents id */
	var bitmapsDataMap = mutableMapOf<Int, Bitmap>()
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

	fun moveToCurrentLocation() {
		googleMap?.animateCamera(
			CameraUpdateFactory.newLatLng(myCurrentLocation ?: return)
		)
	}

}
