package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.*
import grand.app.moon.presentation.home.models.TypeSearchResult
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: SearchResultsFragmentArgs,
	val userLocalUseCase: UserLocalUseCase
) : AndroidViewModel(application) {

	val advertisements = repoShop.getSearchResults(args.search, TypeSearchResult.ADVERTISEMENT)
	val stories = repoShop.getSearchResults(args.search, TypeSearchResult.STORE)
	val nicknames = repoShop.getSearchResults(args.search, TypeSearchResult.NICKNAME)
	val categories = repoShop.getSearchResults(args.search, TypeSearchResult.CATEGORY)

	val adapterAdvertisements = getAdsAdapter()
	val adapterStories = getStoresAdapter()
	val adapterNicknames = getStoresAdapter()
	val adapterCategories = getCategoriesAdapter()

	val filterType = MutableLiveData(TypeSearchResult.ADVERTISEMENT)

	val showEmptyForAds = MutableLiveData(false)
	val showEmptyForStores = MutableLiveData(false)
	val showEmptyForNicknames = MutableLiveData(false)
	val showEmptyForCategories = MutableLiveData(false)
	val showEmptyView = switchMapMultiple2(filterType, showEmptyForAds, showEmptyForStores, showEmptyForNicknames, showEmptyForCategories) {
		when (filterType.value) {
			TypeSearchResult.ADVERTISEMENT -> showEmptyForAds.value == true
			TypeSearchResult.STORE -> showEmptyForStores.value == true
			TypeSearchResult.NICKNAME -> showEmptyForNicknames.value == true
			TypeSearchResult.CATEGORY -> showEmptyForCategories.value == true
			null -> true
		}
	}

	val showLoadingForAds = MutableLiveData(true)
	val showLoadingForStores = MutableLiveData(true)
	val showLoadingForNicknames = MutableLiveData(true)
	val showLoadingForCategories = MutableLiveData(true)
	val showLoadingView = switchMapMultiple2(filterType, showLoadingForAds, showLoadingForStores, showLoadingForNicknames, showLoadingForCategories) {
		when (filterType.value) {
			TypeSearchResult.ADVERTISEMENT -> showLoadingForAds.value == true
			TypeSearchResult.STORE -> showLoadingForStores.value == true
			TypeSearchResult.NICKNAME -> showLoadingForNicknames.value == true
			TypeSearchResult.CATEGORY -> showLoadingForCategories.value == true
			null -> true
		}
	}

	val filterAdvertisementsIsSelected = filterType.map {
		it == TypeSearchResult.ADVERTISEMENT
	}
	val filterStoresIsSelected = filterType.map {
		it == TypeSearchResult.STORE
	}
	val filterNicknamesIsSelected = filterType.map {
		it == TypeSearchResult.NICKNAME
	}
	val filterCategoriesIsSelected = filterType.map {
		it == TypeSearchResult.CATEGORY
	}

	val label = filterType.map { type ->
		type?.let { app.getString(it.stringRes) }.orEmpty()
	}

	fun goBack(view: View) {
		view.findNavController().navigateUp()
	}

	fun changeFilter(filter: TypeSearchResult) {
		filterType.postValue(filter)
	}

}
