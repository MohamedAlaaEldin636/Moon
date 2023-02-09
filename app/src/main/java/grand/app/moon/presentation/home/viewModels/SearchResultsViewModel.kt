package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.showError
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemHomeRvAdvBinding
import grand.app.moon.databinding.ItemSearchResultBinding
import grand.app.moon.databinding.ItemSearchSuggestionsBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.SearchResultsFragmentArgs
import grand.app.moon.presentation.home.SearchSuggestionsFragment
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ResponseSearchResult
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

	val filterAdvertisementsIsSelected = MutableLiveData(true)
	val filterStoresIsSelected = MutableLiveData(false)
	val filterNicknamesIsSelected = MutableLiveData(false)
	val filterCategoriesIsSelected = MutableLiveData(false)

	val adapterAdvertisements = getAdapter()
	val adapterStories = getAdapter()
	val adapterNicknames = getAdapter()
	val adapterCategories = getAdapter()

	val filterType = MutableLiveData(TypeSearchResult.ADVERTISEMENT)

	val label = filterType.map { type ->
		type?.let { app.getString(it.stringRes) }.orEmpty()
	}

	private fun getAdapter() = RVPagingItemCommonListUsage<ItemSearchResultBinding, ResponseSearchResult>(
		R.layout.item_search_result,
		onItemClick = { adapter, binding ->
			val item = (binding.constraintLayout.tag as? String)?.fromJsonInlinedOrNull<ResponseSearchResult>()
				?: return@RVPagingItemCommonListUsage

			General.TODO("ch 1 ${item.storeId} ${item.store?.id} ${userLocalUseCase().id}")
		},
		additionalListenersSetups = { adapter, binding ->
			binding.storeImageImageView.setOnClickListener {
				General.TODO("ch 2")
			}
			binding.favImageView.setOnClickListener {
				General.TODO("ch 2")
			}

			binding.whatsAppConstraintLayout.setOnClickListener {
				General.TODO("ch 2")
			}
			binding.phoneConstraintLayout.setOnClickListener {
				General.TODO("ch 2")
			}
			binding.chatConstraintLayout.setOnClickListener {
				General.TODO("ch 2")
			}
		}
	) { binding, position, item ->
		binding.constraintLayout.tag = item.toJsonInlinedOrNull()

		binding.imageImageView.setupWithGlide {
			load(item.image)
		}

		binding.premiumImageView.isVisible = item.isPremium

		binding.favImageView.setImageResource(
			if (item.isFavorite.orFalse()) R.drawable.item_adv_fav_med_cropped else R.drawable.item_adv_fav_large_cropped
		)

		binding.ratingTextView.text = "( ${item.averageRate?.round(1).orZero()
			.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"

		binding.favsTextView.text = item.favoriteCount.orZero().toStringOrEmpty()

		binding.viewsTextView.text = item.viewsCount.orZero().toStringOrEmpty()

		binding.nameTextView.text = item.title

		binding.timeTextView.text = item.createdAt.orEmpty()

		binding.placeTextView.text = "${item.country?.name.orEmpty()} / ${item.city?.name.orEmpty()}"

		binding.storeImageImageView.setupWithGlide {
			load(item.store?.image)
		}

		binding.storeTextView.text = item.store?.name

		binding.priceTextView.text = item.price?.round(2).orZero().toString()
		binding.currencyTextView.text = item.country?.currency.orEmpty()

		binding.negotiableTextView.isVisible = item.isNegotiable
	}

	fun goBack(view: View) {
		view.findNavController().navigateUp()
	}

	fun changeFilter(filter: TypeSearchResult) {
		filterType.value = filter
	}

}
