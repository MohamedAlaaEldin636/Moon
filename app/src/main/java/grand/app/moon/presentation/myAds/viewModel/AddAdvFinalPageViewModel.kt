package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.net.Uri
import android.provider.MediaStore.Audio.Genres
import android.provider.Settings.Global
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.ItemProperty
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.categories.entity.ItemSubCategory
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.countries.use_case.CountriesUseCase
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragment
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragmentArgs
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragmentDirections
import grand.app.moon.presentation.myAds.model.LocalOrApiItemImage
import grand.app.moon.presentation.myAds.model.LocationData
import grand.app.moon.presentation.myAds.model.ResponseMyAdvDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.SortedMap
import javax.inject.Inject

@HiltViewModel
class AddAdvFinalPageViewModel @Inject constructor(
	//private val homeUseCase: HomeUseCase,
	application: Application,
	val args: AddAdvFinalPageFragmentArgs,
	val useCaseUser: UserLocalUseCase,
	val countriesUseCase: CountriesUseCase,
	val adsUseCase: AdsUseCase,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val response = args.jsonResponseMyAdvDetails.fromJsonInlinedOrNull<ResponseMyAdvDetails>()

	val user = useCaseUser()

	val brands = args.jsonListOfBrands.fromJsonInlinedOrNull<List<ItemSubCategory>>().orEmpty()

	val addressLabel: CharSequence = app.getAsRequiredText(getString(R.string.address_advertisement))

	val price = MutableLiveData(response?.price?.toString().orEmpty())

	val priceBeforeDiscount = MutableLiveData(response?.priceBeforeDiscount?.toString().orEmpty())

	val title = MutableLiveData(response?.title.orEmpty())

	val negotiable = MutableLiveData(response?.isNegotiable.orFalse())

	val description = MutableLiveData(response?.description.orEmpty())

	private val _countries =
		MutableStateFlow<Resource<BaseResponse<List<Country>>>>(Resource.Default)
	val countries: Flow<Resource<BaseResponse<List<Country>>>> = _countries
	var cities = emptyList<Country>()
	val showCitiesPopupMenu = MutableLiveData(false)
	val selectedCity = MutableLiveData<Country>()

	var storeCategories = emptyList<IdAndName>()
	val showCategoriesPopupMenu = MutableLiveData(false)
	val selectedCategory = MutableLiveData<IdAndName?>()

	var storeSubCategories = emptyList<IdAndName>()
	val showSubCategoriesPopupMenu = MutableLiveData(false)
	val selectedSubCategory = MutableLiveData<IdAndName?>()

	val selectedBrand = MutableLiveData<ItemSubCategory>(
		brands.firstOrNull { it.id == response?.brand?.id }.also {
			MyLogger.e("ad details cycle -> $brands ${response?.brand}")
		}
	)

	val properties = MutableLiveData<List<ItemProperty>>()

	val mapOfProperties = MutableLiveData<SortedMap<Int, ItemProperty>>()

	val locationData = MutableLiveData<LocationData>(
		response?.latitude?.let { latitude ->
			response.longitude?.let { longitude ->
				response.address?.let { address ->
					LocationData(latitude, longitude, address)
				}
			}
		}
	)

	val listOfImages = MutableLiveData<List<LocalOrApiItemImage>>(
		response?.images?.map { LocalOrApiItemImage.Api(it) }.orEmpty()
	)
	val indexToShowImagesPopupMenu = MutableLiveData<Int>()
	val beforeCheckPermissionsIndexToShowImagesPopupMenu = MutableLiveData<Int>()

	fun setImages(list: List<LocalOrApiItemImage>) {
		listOfImages.value = list
	}

	fun goToMapToGetAddress(fragment: AddAdvFinalPageFragment) {
		fragment.findNavController().navDeepToLocationSelection(
			locationData.value?.latitude,
			locationData.value?.longitude,
		)
	}

	fun getCountries() {
		countriesUseCase().onEach {
			_countries.value = it
		}.launchIn(viewModelScope)
	}

	fun addAdvertisement(fragment: AddAdvFinalPageFragment) {
		val context = (fragment as? Fragment)?.context ?: return

		val isStore = useCaseUser().isStore.orFalse()

		if (locationData.value == null || selectedCity.value == null || price.value.isNullOrEmpty()
			|| (brands.isNotEmpty() && selectedBrand.value == null) /*|| description.value.isNullOrEmpty()*/
			|| mapOfProperties.value.orEmpty().values.any {
				if (it.type != 1 && it.type != 3) false else {
					it.valueId == null && it.valueString == null && it.valueBoolean == null
				}
			}
			|| listOfImages.value.isNullOrEmpty() || title.value.isNullOrEmpty()
			|| (isStore && selectedCategory.value == null) || (isStore && selectedSubCategory.value == null)){
			return fragment.showError(fragment.getString(R.string.all_fields_required))
		}

		if (isStore && priceBeforeDiscount.value?.toIntOrNull() != null
			&& price.value?.toIntOrNull().orZero() >= priceBeforeDiscount.value?.toIntOrNull().orZero()) {
			return fragment.showError(fragment.getString(R.string.price_before_and_after_check))
		}

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				if (response != null) {
					adsUseCase.updateAdvertisement(
						isStore,
						response.id.orZero(),
						args.idOfMainCategory,
						args.idOfSubCategory,
						listOfImages.value.orEmpty().mapNotNull {
							(it as? LocalOrApiItemImage.Local)?.uri
								?.createMultipartBodyPart(context.applicationContext, "images[]")
						},
						title.value.orEmpty(),
						locationData.value?.latitude.orEmpty(),
						locationData.value?.longitude.orEmpty(),
						locationData.value?.address.orEmpty(),
						selectedCity.value?.id.orZero(),
						price.value?.toFloatOrNull().orZero(),
						negotiable.value.orFalse(),
						selectedBrand.value?.id,
						description.value.orEmpty(),
						mapOfProperties.value.orEmpty().mapNotNull { (_, value) ->
							when {
								value.valueId != null -> {
									value.valueId.orZero() to null
								}
								value.valueString != null -> {
									value.id.orZero() to value.valueString
								}
								else /*value.valueBoolean != null*/ -> {
									if (value.valueBoolean == true) {
										value.id.orZero() to null
									}else {
										null
									}
								}
							}
						},
						priceBeforeDiscount.value?.toFloatOrNull(),
						selectedCategory.value?.id,
						selectedSubCategory.value?.id,
					)
				}else {
					adsUseCase.addAdvertisement(
						isStore,
						args.idOfMainCategory,
						args.idOfSubCategory,
						listOfImages.value.orEmpty().mapNotNull {
							(it as? LocalOrApiItemImage.Local)?.uri
								?.createMultipartBodyPart(context.applicationContext, "images[]")
						},
						title.value.orEmpty(),
						locationData.value?.latitude.orEmpty(),
						locationData.value?.longitude.orEmpty(),
						locationData.value?.address.orEmpty(),
						selectedCity.value?.id.orZero(),
						price.value?.toFloatOrNull().orZero(),
						negotiable.value.orFalse(),
						selectedBrand.value?.id,
						description.value.orEmpty(),
						mapOfProperties.value.orEmpty().mapNotNull { (_, value) ->
							when {
								value.valueId != null -> {
									value.valueId.orZero() to null
								}
								value.valueString != null -> {
									value.id.orZero() to value.valueString
								}
								else /*value.valueBoolean != null*/ -> {
									if (value.valueBoolean == true) {
										value.id.orZero() to null
									}else {
										null
									}
								}
							}
						},
						priceBeforeDiscount.value?.toFloatOrNull(),
						selectedCategory.value?.id,
						selectedSubCategory.value?.id,
					)
				}
			}
		) {
			fragment.showMessage(context.getString(R.string.done_successfully))

			val navController = fragment.findNavController()

			navController.navigateSafely(
				AddAdvFinalPageFragmentDirections.actionDestLocationSelectionToHomeFragment2()
			)

			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.presentation.myAds.dest.my.adv.details.json.of.response.my.adv.details",
				paths = arrayOf(it.toJsonInlinedOrNull().orEmpty())
			)
		}
	}

}
