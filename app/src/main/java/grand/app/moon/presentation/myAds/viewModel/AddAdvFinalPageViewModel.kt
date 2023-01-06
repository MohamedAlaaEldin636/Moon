package grand.app.moon.presentation.myAds.viewModel

import android.app.Application
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.text.buildSpannedString
import androidx.lifecycle.*
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.MyApplication
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
import grand.app.moon.extensions.app
import grand.app.moon.extensions.fromJsonInlinedOrNull
import grand.app.moon.extensions.getAsRequiredText
import grand.app.moon.extensions.getString
import grand.app.moon.presentation.categories.AddAdvSubCategoriesListFragmentArgs
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragment
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragmentArgs
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

	val address = MutableLiveData("")

	// todo cities and city id ba2a w kda isa.
	//accountRepository.getCountries()

	val price = MutableLiveData("")

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

	/*init {
		adsUseCase.getFilterDetails2(args.idOfMainCategory, args.idOfSubCategory).onEach {
			_properties.value = it
		}.launchIn(viewModelScope)
	}*/

	fun goToMapToGetAddress(fragment: AddAdvFinalPageFragment) {
		// todo ....
		Toast.makeText(fragment.requireContext(), "go to maps ${price.value}", Toast.LENGTH_SHORT).show()
	}

	fun getCountries() {
		countriesUseCase().onEach {
			_countries.value = it
		}.launchIn(viewModelScope)
	}

}
