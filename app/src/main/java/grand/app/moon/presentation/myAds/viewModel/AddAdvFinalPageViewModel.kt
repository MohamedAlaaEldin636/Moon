package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.net.Uri
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.createMultipartBodyPart
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.ItemProperty
import grand.app.moon.domain.ads.ResponseFilterDetails
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.categories.entity.ItemSubCategory
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.domain.countries.use_case.CountriesUseCase
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.categories.AddAdvSubCategoriesListFragmentArgs
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragment
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragmentArgs
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragmentDirections
import grand.app.moon.presentation.myAds.model.LocationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAdvFinalPageViewModel @Inject constructor(
	//private val homeUseCase: HomeUseCase,
	application: Application,
	val args: AddAdvFinalPageFragmentArgs,
	useCaseUser: UserLocalUseCase,
	val countriesUseCase: CountriesUseCase,
	accountRepository: AccountRepository,
	val adsUseCase: AdsUseCase
) : AndroidViewModel(application) {

	val user = useCaseUser()

	val brands = args.jsonListOfBrands.fromJsonInlinedOrNull<List<ItemSubCategory>>().orEmpty()

	val addressLabel: CharSequence = app.getAsRequiredText(getString(R.string.address_advertisement))

	val price = MutableLiveData("")

	val title = MutableLiveData("")

	val negotiable = MutableLiveData(false)

	val storeCategory = MutableLiveData("")

	val description = MutableLiveData("")

	// todo list of available store categories asln isa oh fuck if is store asln ya zaki isa..

	private val _countries =
		MutableStateFlow<Resource<BaseResponse<List<Country>>>>(Resource.Default)
	val countries: Flow<Resource<BaseResponse<List<Country>>>> = _countries
	var cities = emptyList<Country>()
	val showCitiesPopupMenu = MutableLiveData(false)
	val selectedCity = MutableLiveData<Country>()

	val selectedBrand = MutableLiveData<ItemSubCategory>()

	val properties = MutableLiveData<List<ItemProperty>>()

	val mapOfProperties = MutableLiveData<Map<Int, ItemProperty>>()

	val locationData = MutableLiveData<LocationData>()

	/** 1, 2, 4, 5, 6 --- Max. num is 22 also add @Header to make timeout infinity */
	val mapOfImages = MutableLiveData(emptyMap<Int, List<Uri>>())
	val showImagesPopupMenu = MutableLiveData(false)

	fun addImage(tag: Int, uri: Uri) {
		mapOfImages.value = mapOfImages.value.orEmpty().toMutableMap().also { mutableMap ->
			mutableMap[tag] = mutableMap[tag].orEmpty().toMutableList().also {
				if (tag != 1) {
					it.clear()
				}

				it.add(uri)
			}.toList()
		}.toMap()
	}

	/*init {
		adsUseCase.getFilterDetails2(args.idOfMainCategory, args.idOfSubCategory).onEach {
			_properties.value = it
		}.launchIn(viewModelScope)
	}*/

	fun goToMapToGetAddress(fragment: AddAdvFinalPageFragment) {
		fragment.findNavController().navigate(
			AddAdvFinalPageFragmentDirections.actionDestAddAdvFinalPageToDestLocationSelection(
				true,
				locationData.value?.latitude,
				locationData.value?.longitude,
			)
		)
	}

	fun getCountries() {
		countriesUseCase().onEach {
			_countries.value = it
		}.launchIn(viewModelScope)
	}

	fun addAdvertisement(fragment: AddAdvFinalPageFragment) {
		val context = (fragment as? Fragment)?.context ?: return

		// todo if boolean in dynamic data must be selectewd and even if false always true isa.
		if (locationData.value == null || selectedCity.value == null || price.value.isNullOrEmpty()
			|| (brands.isNotEmpty() && selectedBrand.value == null) /*|| description.value.isNullOrEmpty()*/
			|| mapOfProperties.value.orEmpty().values.any {
				if (it.type != 1 && it.type != 3) false else {
					it.valueId == null && it.valueString == null && it.valueBoolean == null
				}
			}
			|| mapOfImages.value.orEmpty().all { it.value.isEmpty() } || title.value.isNullOrEmpty()){
			return fragment.showError(fragment.getString(R.string.all_fields_required))
		}

		fragment.handleRetryAbleActionCancellable(
			action = {
				adsUseCase.addAdvertisement(
					args.idOfMainCategory,
					args.idOfSubCategory,
					mapOfImages.value.orEmpty().flatMap {
						it.value
					}.mapNotNull {
						it.createMultipartBodyPart(context.applicationContext, "images[]")
					},
					title.value.orEmpty(),
					locationData.value?.latitude.orEmpty(),
					locationData.value?.longitude.orEmpty(),
					locationData.value?.address.orEmpty(),
					selectedCity.value?.id.orZero(),
					price.value?.toIntOrNull().orZero(),
					negotiable.value.orFalse(),
					selectedBrand.value?.id,
					description.value.orEmpty(),
					mapOfProperties.value.orEmpty().mapNotNull { (_, value) ->
						when {
							value.valueId != null -> {
								value.valueId.orZero()
							}
							value.valueString != null -> {
								value.id.orZero()
							}
							else /*value.valueBoolean != null*/ -> {
								if (value.valueBoolean == true) {
									value.id.orZero()
								}else {
									null
								}
							}
						}
					}
				)
			}
		) {
			fragment.showMessage(context.getString(R.string.ad_has_been_added_successfully))

			fragment.findNavController().navigateSafely(
				AddAdvFinalPageFragmentDirections.actionDestLocationSelectionToHomeFragment2()
			)
		}
	}

}
