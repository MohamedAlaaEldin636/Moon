package grand.app.moon.presentation.myAds

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentMyAdsBinding
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.domain.shop.ResponseStoreSubCategory
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myAds.viewModel.MyAdsViewModel

@AndroidEntryPoint
class MyAdsFragment : BaseFragment<FragmentMyAdsBinding>()  {

	private val viewModel by viewModels<MyAdsViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_my_ads

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val isStore = viewModel.userLocalUseCase().isStore.orFalse()
		MyLogger.e("dhuiehdiuewhd $isStore")
		viewModel.showStoreData.value = isStore
		if (isStore) {
			binding.recyclerViewStoreCategories.setupWithRVItemCommonListUsage(
				viewModel.adapterCategories,
				true,
				1
			)
			binding.recyclerViewStoreSubCategories.setupWithRVItemCommonListUsage(
				viewModel.adapterSubCategories,
				true,
				1
			)

			handleRetryAbleActionOrGoBack(
				action = {
					viewModel.repoShop.getMyStoreCategoriesAndSubCategoriesAllPaginationPages()
				},
				hideLoadingCode = {}
			) { pair ->
				viewModel.allCategories.value = listOf(IdAndName(null, getString(R.string.all))) + pair.first
				viewModel.allSubCategories.value = listOf(ResponseStoreSubCategory(null, getString(R.string.all), null)) + pair.second

				viewModel.adapterCategories.submitList(viewModel.allCategories.value.orEmpty())
				//viewModel.adapterSubCategories.submitList(viewModel.allSubCategories.value.orEmpty())

				viewModel.shownSubCategories.observe(viewLifecycleOwner) {
					viewModel.adapterSubCategories.submitList(it.orEmpty())
				}

				viewModel.additionalFilter.observe(viewLifecycleOwner) { (selectedCategory, selectedSubCategory) ->
					viewModel.performAdditionalFiltering(selectedCategory, selectedSubCategory)
				}

				viewModel.searchNow(this)
			}
		}else {
			viewModel.searchNow(this)
		}

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			false,
			1
		)

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<NewAdvertisementChange> { change ->
			when (change.state) {
				NewAdvertisementState.BECAME_PREMIUM -> {
					viewModel.adapter.list.indexOfFirstOrNull { it.id == change.id }?.also {
						viewModel.adapter.list[it].makePremium()

						viewModel.adapter.notifyItemChanged(it)
					}
				}
				NewAdvertisementState.DELETED -> {
					viewModel.adapter.list.indexOfFirstOrNull { it.id == change.id }?.also {
						viewModel.adapter.deleteAt(it)
					}
				}
			}
		}
	}

	data class NewAdvertisementChange(
		var id: Int,
		var state: NewAdvertisementState,
	)

	enum class NewAdvertisementState {
		BECAME_PREMIUM, DELETED
	}

}
