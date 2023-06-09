package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemDynamicFilterBooleanBinding
import grand.app.moon.databinding.ItemDynamicFilterRangedTextBinding
import grand.app.moon.databinding.ItemDynamicFilterSelectionBinding
import grand.app.moon.databinding.ItemDynamicFilterTextBinding
import grand.app.moon.domain.ads.DynamicFilterProperty
import grand.app.moon.domain.ads.ResponseFilterProperties
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.categories.entity.ItemSubCategory
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.setCompoundDrawablesRelativeWithIntrinsicBoundsEnd
import grand.app.moon.presentation.base.extensions.hideKeyboard
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.*
import grand.app.moon.presentation.myStore.model.ResponseArea
import grand.app.moon.presentation.myStore.model.ResponseCity
import javax.inject.Inject

@HiltViewModel
class FilterAllViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: FilterAllFragmentArgs,
) : AndroidViewModel(application) {

	val initialFilter = FilterAllFragment.Filter.fromSpecialString(args.initialSpecialStringFiltering)

	val showDataOfAds = MutableLiveData(args.forAdsNotStores)

	private val allCategoriesWithSubCategories = repoShop.getCategoriesWithSubCategoriesAndBrands()

	private val country = repoShop.getSelectedCountry()
	private val citiesWithAreas = country?.cities.orEmpty()

	val searchQuery = MutableLiveData(initialFilter.search)

	private val selectedCategory = MutableLiveData<ItemCategory?>(
		allCategoriesWithSubCategories.firstOrNull {
			initialFilter.categoryId == it.id
		}
	)
	private val selectedSubCategory = MutableLiveData<ItemSubCategory?>(
		if (initialFilter.categoryId == null) null else {
			allCategoriesWithSubCategories.firstOrNull { itemCategory ->
				itemCategory.id == initialFilter.categoryId
			}?.subCategories?.firstOrNull {
				it.id == initialFilter.subCategoryId
			}
		}
	)
	val showBrand = selectedCategory.map {
		args.forAdsNotStores && it?.brands.isNullOrEmpty().not()
	}
	private val selectedBrand = MutableLiveData<ItemSubCategory?>(
		if (initialFilter.categoryId == null) null else {
			allCategoriesWithSubCategories.firstOrNull { itemCategory ->
				itemCategory.id == initialFilter.categoryId
			}?.brands?.firstOrNull {
				it.id == initialFilter.brandId
			}
		}
	)
	val mainCategory = selectedCategory.map {
		it?.name.letIfNullOrEmpty { app.getString(R.string.main_section) }
	}
	val subCategory = selectedSubCategory.map {
		it?.name ?: app.getString(R.string.sub_section)
	}
	val brand = selectedBrand.map {
		it?.name ?: app.getString(R.string.brand)
	}

	private val selectedCity = MutableLiveData<ResponseCity?>(
		citiesWithAreas.firstOrNull {
			it.id == initialFilter.cityId
		}
	)
	val city = selectedCity.map {
		it?.name ?: app.getString(R.string.city)
	}

	private val selectedAreas = MutableLiveData<List<ResponseArea>?>(
		if (initialFilter.cityId == null) null else {
			val list = citiesWithAreas.firstOrNull {
				it.id == initialFilter.cityId
			}?.areas?.filter {
				it.id in initialFilter.areasIds.orEmpty()
			}

			if (list.isNullOrEmpty()) null else list
		}
	)
	val area = selectedAreas.map { list ->
		if (list.isNullOrEmpty()) {
			app.getString(R.string.area)
		}else {
			list.joinToString(" - ") { it.name.orEmpty() }
		}
	}

	val minPrice = MutableLiveData(initialFilter.minPrice.toStringOrEmpty())
	val maxPrice = MutableLiveData(initialFilter.maxPrice.toStringOrEmpty())

	val selectedSortBy = MutableLiveData<FilterAllFragment.SortBy?>(initialFilter.sortBy)

	val selectedAdType = MutableLiveData<FilterAllFragment.AdType?>(initialFilter.adType)

	val fiveStarsIsSelected = MutableLiveData(initialFilter.rating == 5)
	val fourStarsIsSelected = MutableLiveData(initialFilter.rating == 4)
	val threeStarsIsSelected = MutableLiveData(initialFilter.rating == 3)
	val twoStarsIsSelected = MutableLiveData(initialFilter.rating == 2)
	val oneStarIsSelected = MutableLiveData(initialFilter.rating == 1)

	val responseFilterProperties = MutableLiveData<ResponseFilterProperties?>()

	val adapter = RVItemCommonListUsageWithDifferentItems<DynamicFilterProperty>(
		getLayoutRes = { position ->
			when (list[position]) {
				is DynamicFilterProperty.Text -> R.layout.item_dynamic_filter_text
				is DynamicFilterProperty.Checked -> R.layout.item_dynamic_filter_boolean
				is DynamicFilterProperty.RangedText -> R.layout.item_dynamic_filter_ranged_text
				is DynamicFilterProperty.Selection -> R.layout.item_dynamic_filter_selection
			}
		},
		onItemClick = { adapter, binding ->
			val fragment = binding.root.findFragmentOrNull<FilterAllFragment>() ?: return@RVItemCommonListUsageWithDifferentItems

			val position = binding.root.tag as? Int ?: return@RVItemCommonListUsageWithDifferentItems

			val item = adapter.list[position]

			when {
				binding is ItemDynamicFilterBooleanBinding && item is DynamicFilterProperty.Checked -> {
					responseFilterProperties.value = responseFilterProperties.value?.let {
						it.copy(
							properties = it.properties.copyUpdateItem(position, item.copy(isSelected = item.isSelected.not()))
						)
					}
				}
				binding is ItemDynamicFilterSelectionBinding && item is DynamicFilterProperty.Selection -> {
					binding.root.findNavController().observeOnceForSelection(
						false,
						item.data,
						item.selectedData,
						fragment,
						item.id.toString()
					) { list ->
						responseFilterProperties.value = responseFilterProperties.value?.let {
							it.copy(
								properties = it.properties.copyUpdateItem(position, item.copy(selectedData = list))
							)
						}
					}
				}
				binding is ItemDynamicFilterTextBinding && item is DynamicFilterProperty.Text -> {}
				binding is ItemDynamicFilterRangedTextBinding && item is DynamicFilterProperty.RangedText -> {}
			}
		},
		additionalListenersSetups = { _, binding ->
			when (binding) {
				is ItemDynamicFilterTextBinding -> {
					binding.editText.doAfterTextChanged { newText ->
						val position = binding.root.tag as? Int ?: return@doAfterTextChanged

						(responseFilterProperties.value?.properties?.getOrNull(position) as? DynamicFilterProperty.Text)
							?.value = newText.toStringOrEmpty()
					}
				}
				is ItemDynamicFilterRangedTextBinding -> {
					binding.fromEditText.doAfterTextChanged { newText ->
						val position = binding.root.tag as? Int ?: return@doAfterTextChanged

						(responseFilterProperties.value?.properties?.getOrNull(position) as? DynamicFilterProperty.RangedText)
							?.from = newText.toStringOrEmpty()
					}

					binding.fromEditText.doAfterTextChanged { newText ->
						val position = binding.root.tag as? Int ?: return@doAfterTextChanged

						(responseFilterProperties.value?.properties?.getOrNull(position) as? DynamicFilterProperty.RangedText)
							?.to = newText.toStringOrEmpty()
					}
				}
				else -> {}
			}
		}
	) { binding, position, item ->
		binding.root.tag = position

		when {
			binding is ItemDynamicFilterBooleanBinding && item is DynamicFilterProperty.Checked -> {
				binding.textView.text = item.title

				binding.textView.setCompoundDrawablesRelativeWithIntrinsicBoundsEnd(
					if (item.isSelected) R.drawable.ic_switch_on_1 else R.drawable.ic_switch_off_1
				)
			}
			binding is ItemDynamicFilterSelectionBinding && item is DynamicFilterProperty.Selection -> {
				binding.textView.text = if (item.selectedData.isEmpty()) item.title else {
					item.selectedData.joinToString(" - ") { it.name.orEmpty() }
				}
			}
			binding is ItemDynamicFilterTextBinding && item is DynamicFilterProperty.Text -> {
				binding.editText.hint = item.title

				binding.editText.setText(item.value)
			}
			binding is ItemDynamicFilterRangedTextBinding && item is DynamicFilterProperty.RangedText -> {
				binding.fromEditText.hint = "${app.getString(R.string.from)} ( ${item.title} )"
				binding.fromEditText.setText(item.from)

				binding.toEditText.hint = "${app.getString(R.string.to)} ( ${item.title} )"
				binding.toEditText.setText(item.to)
			}
		}
	}

	fun toggleStars(numberOfStars: Int) {
		when (numberOfStars) {
			5 -> {
				if (fiveStarsIsSelected.value != true) {
					fiveStarsIsSelected.value = true
					fourStarsIsSelected.value = false
					threeStarsIsSelected.value = false
					twoStarsIsSelected.value = false
					oneStarIsSelected.value = false
				}
			}
			4 -> {
				if (fourStarsIsSelected.value != true) {
					fiveStarsIsSelected.value = false
					fourStarsIsSelected.value = true
					threeStarsIsSelected.value = false
					twoStarsIsSelected.value = false
					oneStarIsSelected.value = false
				}
			}
			3 -> {
				if (threeStarsIsSelected.value != true) {
					fiveStarsIsSelected.value = false
					fourStarsIsSelected.value = false
					threeStarsIsSelected.value = true
					twoStarsIsSelected.value = false
					oneStarIsSelected.value = false
				}
			}
			2 -> {
				if (twoStarsIsSelected.value != true) {
					fiveStarsIsSelected.value = false
					fourStarsIsSelected.value = false
					threeStarsIsSelected.value = false
					twoStarsIsSelected.value = true
					oneStarIsSelected.value = false
				}
			}
			1 -> {
				if (oneStarIsSelected.value != true) {
					fiveStarsIsSelected.value = false
					fourStarsIsSelected.value = false
					threeStarsIsSelected.value = false
					twoStarsIsSelected.value = false
					oneStarIsSelected.value = true
				}
			}
		}
	}

	fun pickMainCategory(view: View) {
		val fragment = view.findFragmentOrNull<FilterAllFragment>() ?: return

		if (allCategoriesWithSubCategories.isEmpty()) return

		view.showPopup(
			allCategoriesWithSubCategories.map { it.name.orEmpty() },
			listener = { menuItem ->
				val newSelection = allCategoriesWithSubCategories.firstOrNull { it.name == menuItem.title }

				if (newSelection != selectedCategory.value) {
					selectedCategory.value = newSelection
					selectedSubCategory.value = null
					selectedBrand.value = null

					if (args.forAdsNotStores) {
						fragment.handleRetryAbleActionOrGoBack(
							action = {
								repoShop.getFilterProperties(newSelection?.id.orZero(), null)
							}
						) {
							responseFilterProperties.value = it
						}
					}
				}
			}
		)
	}
	fun pickSubCategory(view: View) {
		val fragment = view.findFragmentOrNull<FilterAllFragment>() ?: return

		if (selectedCategory.value == null) return fragment.showMessage(fragment.getString(R.string.pick_main_category_firstly))

		val allSubCategories = selectedCategory.value?.subCategories.orEmpty()

		if (allSubCategories.isEmpty()) return fragment.showMessage(fragment.getString(R.string.no_sub_categories_found))

		view.showPopup(
			allSubCategories.map { it.name.orEmpty() },
			listener = { menuItem ->
				val newSelection = allSubCategories.firstOrNull { it.name == menuItem.title }

				if (newSelection != selectedSubCategory.value) {
					selectedSubCategory.value = newSelection

					if (args.forAdsNotStores) {
						fragment.handleRetryAbleActionOrGoBack(
							action = {
								repoShop.getFilterProperties(selectedCategory.value?.id.orZero(), newSelection?.id.orZero())
							}
						) {
							responseFilterProperties.value = it
						}
					}
				}
			}
		)
	}
	fun pickBrand(view: View) {
		val fragment = view.findFragmentOrNull<FilterAllFragment>() ?: return

		if (selectedCategory.value == null) return fragment.showMessage(fragment.getString(R.string.pick_main_category_firstly))

		val allBrands = selectedCategory.value?.brands.orEmpty()

		if (allBrands.isEmpty()) return fragment.showMessage(fragment.getString(R.string.no_brands_found))

		view.showPopup(
			allBrands.map { it.name.orEmpty() },
			listener = { menuItem ->
				val newSelection = allBrands.firstOrNull { it.name == menuItem.title }

				if (newSelection != selectedBrand.value) {
					selectedBrand.value = newSelection
				}
			}
		)
	}

	fun pickCity(view: View) {
		val listOfIdAndName = citiesWithAreas.map {
			IdAndName(it.id, it.name)
		}

		val fragment = view.findFragmentOrNull<FilterAllFragment>() ?: return

		fragment.setFragmentResultListenerUsingJson<IdAndName>(SingleOrMultiSelectionFragment.KEY_RESULT_SINGLE_SELECTION) {
			changeSelectedCity(it.id.orZero())
		}

		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.single.or.multi.selection",
			paths = arrayOf(
				true.toString(),
				listOfIdAndName.toJsonInlinedOrNull(),
				listOfNotNull(selectedCity.value).toJsonInlinedOrNull(),
				SingleOrMultiSelectionFragment.KEY_RESULT_SINGLE_SELECTION
			)
		)
	}

	fun pickArea(view: View) {
		val fragment = view.findFragmentOrNull<FilterAllFragment>() ?: return

		if (selectedCity.value == null) return fragment.showMessage(fragment.getString(R.string.select_city_firslty_4))

		val allAreas = selectedCity.value?.areas.orEmpty()

		if (allAreas.isEmpty()) return fragment.showMessage(fragment.getString(R.string.no_cities_found))

		val listOfIdAndName = allAreas.map {
			IdAndName(it.id, it.name)
		}

		fragment.setFragmentResultListenerUsingJson<List<IdAndName>>(SingleOrMultiSelectionFragment.KEY_RESULT_MULTI_SELECTION) { list ->
			changeSelectedAreas(list.map { it.id.orZero() })
		}

		view.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.single.or.multi.selection",
			paths = arrayOf(
				false.toString(),
				listOfIdAndName.toJsonInlinedOrNull(),
				selectedAreas.value.orEmpty().map { IdAndName(it.id, it.name) }.toJsonInlinedOrNull(),
				SingleOrMultiSelectionFragment.KEY_RESULT_MULTI_SELECTION
			)
		)
	}

	fun changeSelectedSortBy(sortBy: FilterAllFragment.SortBy) {
		selectedSortBy.value = sortBy
	}

	fun changeSelectedAdType(adType: FilterAllFragment.AdType) {
		selectedAdType.value = adType
	}

	private fun changeSelectedCity(id: Int) {
		if (selectedCity.value?.id != id) {
			selectedCity.value = citiesWithAreas.firstOrNull { it.id == id }
			selectedAreas.value = null
		}
	}

	private fun changeSelectedAreas(ids: List<Int>) {
		selectedAreas.value = selectedCity.value?.areas.orEmpty().filter { it.id in ids }
	}

	fun filter(view: View) {
		val fragment = view.findFragmentOrNull<FilterAllFragment>() ?: return

		val minPrice = minPrice.value?.toFloatOrNull()
		val maxPrice = maxPrice.value?.toFloatOrNull()

		// Check that max price is at least same or greater than min price.
		if (minPrice != null && maxPrice != null) {
			if (minPrice > maxPrice) return fragment.showMessage(fragment.getString(R.string.warning_of_price_range))
		}

		fragment.hideKeyboardNew()

		if (showDataOfAds.value == true) {
			fragment.navGraphViewModel.filter = FilterAllFragment.Filter(
				searchQuery.value,
				selectedCategory.value?.id,
				selectedSubCategory.value?.id,
				selectedBrand.value?.id,
				minPrice,
				maxPrice,
				selectedCity.value?.id,
				selectedAreas.value?.map { it.id.orZero() },
				adapter.list,
				selectedSortBy.value,
				selectedAdType.value,
				getSelectedStar(),
				initialFilter.adSpecificType
			)

			val selectedCategory = selectedCategory.value
			if (args.forceReturnResult) {
				fragment.setFragmentResultUsingJson(
					FilterAllFragment::class.java.name,
					fragment.navGraphViewModel.filter.toSpecialString().orStringNullIfNullOrEmpty()
				)

				fragment.findNavController().navigateUp()
			}else {
				if (selectedCategory != null) {
					AllAdsOfCategoryFragment.launch(
						fragment.findNavController(),
						fragment.navGraphViewModel.filter,
						selectedCategory.name.orEmpty(),
						selectedCategory.id.orZero()
					)
				}else {
					AllAdsFragment.launch(
						fragment.findNavController(),
						fragment.navGraphViewModel.filter,
						fragment.getString(R.string.advertisements_8)
					)
				}
			}
		}else {
			// Ignore only sort as it is in the previous screen, but other stuff in previous screen keep
			// them according to bakrey isa.
			val filter = AllStoresFragment.Filter(
				searchQuery.value,
				selectedCategory.value?.id,
				selectedSubCategory.value?.id,
				selectedCity.value?.id,
				selectedAreas.value?.map { it.id.orZero() },
				null,
				getSelectedStar()
			)

			fragment.setFragmentResultUsingJson(
				FilterAllFragment::class.java.name,
				filter
			)

			fragment.findNavController().navigateUp()
		}
	}

	private fun getSelectedStar(): Int? {
		return when {
			fiveStarsIsSelected.value == true -> 5
			fourStarsIsSelected.value == true -> 4
			threeStarsIsSelected.value == true -> 3
			twoStarsIsSelected.value == true -> 2
			oneStarIsSelected.value == true -> 1
			else -> null
		}
	}

	private fun NavController.observeOnceForSelection(
		isSingleNotMultiSelection: Boolean,
		wholeData: List<IdAndName>,
		currentlySelectedData: List<IdAndName>,
		fragment: Fragment,
		key: String,
		onSelection: (List<IdAndName>) -> Unit
	) {
		if (isSingleNotMultiSelection) {
			fragment.setFragmentResultListenerUsingJson<IdAndName>(key) {
				onSelection(listOf(it))
			}
		}else {
			fragment.setFragmentResultListenerUsingJson<List<IdAndName>>(key) { list ->
				onSelection(list)
			}
		}

		navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.single.or.multi.selection",
			paths = arrayOf(
				isSingleNotMultiSelection.toString(),
				wholeData.toJsonInlinedOrNull().orEmpty(),
				currentlySelectedData.toJsonInlinedOrNull().orEmpty(),
				key
			)
		)

		/*if (isSingleNotMultiSelection) {
			fragment.observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNullOnlyOnce<IdAndName>(key) {
				onSelection(listOf(it))
			}
		}else {
			fragment.observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNullOnlyOnce<List<IdAndName>>(key) {
				onSelection(it)
			}
		}

		navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.single.or.multi.selection",
			paths = arrayOf(
				isSingleNotMultiSelection.toString(),
				wholeData.toJsonInlinedOrNull().orEmpty(),
				currentlySelectedData.toJsonInlinedOrNull().orEmpty(),
				key
			)
		)*/
	}

}
