package grand.app.moon.presentation.myStore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentStoreClientsReviewsBinding
import grand.app.moon.databinding.FragmentStoreSocialMediaBinding
import grand.app.moon.domain.shop.ResponseStoreSocialMedia
import grand.app.moon.extensions.RetryAbleFlow
import grand.app.moon.extensions.handleRetryAbleActionOrGoBackNullable
import grand.app.moon.extensions.minLengthZerosPrefix
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myStore.viewModel.StoreClientsReviewsViewModel
import grand.app.moon.presentation.myStore.viewModel.StoreSocialMediaViewModel

@AndroidEntryPoint
class StoreClientsReviewsFragment : BaseFragment<FragmentStoreClientsReviewsBinding>() {

	private val viewModel by viewModels<StoreClientsReviewsViewModel>()

	val retryAbleFlow by lazy {
		RetryAbleFlow(
			this,
			getFlow = {
				viewModel.repoShop.getClientsReviews(
					viewModel.name.value,
					viewModel.dateFrom.value?.fromUiToApiDate(),
					viewModel.dateTo.value?.fromUiToApiDate(),
				)
			},
			collectLatestAction = {
				viewModel.adapter.submitData(it)
			},
			onRetry = {
				viewModel.adapter.refresh()
			}
		)
	}

	override fun getLayoutId(): Int = R.layout.fragment_store_clients_reviews

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter.withDefaultHeaderAndFooterAdapters(),
			false,
			1
		)

		retryAbleFlow.collectLatest()
	}

	private fun String.fromUiToApiDate(): String? {
		val array = if (contains(" / ")) split(" / ") else return null

		val day = array.getOrNull(0)?.toIntOrNull() ?: return null
		val month = array.getOrNull(1)?.toIntOrNull() ?: return null
		val year = array.getOrNull(2)?.toIntOrNull() ?: return null

		return "${year.toString().minLengthZerosPrefix(4)}-" +
			"${month.toString().minLengthZerosPrefix(2)}-" +
			day.toString().minLengthZerosPrefix(2)
	}

}
