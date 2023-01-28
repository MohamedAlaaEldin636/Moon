package grand.app.moon.presentation.myStore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.LottieDrawable
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentExploreInShopInfoBinding
import grand.app.moon.databinding.ItemLottieAppWarningBinding
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myStore.viewModel.ExploreInShopInfoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreInShopInfoFragment : BaseFragment<FragmentExploreInShopInfoBinding>() {

	private val viewModel by viewModels<ExploreInShopInfoViewModel>()

	val retryAbleFlow by lazy {
		RetryAbleFlow(
			this,
			getFlow = {
				viewModel.repoShop.getExplores2(
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

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				viewModel.repoShop.getExploresRemainingCount()
			}
		) {
			viewModel.remainingExploreCount.value = it
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_explore_in_shop_info

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

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean> {
			viewModel.adapter.refresh()
		}

		_binding?.lottieFrameLayout?.removeAllViews()
		_binding?.lottieFrameLayout?.addView(ItemLottieAppWarningBinding.inflate(layoutInflater).root)
	}

	override fun onDestroyView() {
		_binding?.lottieFrameLayout?.removeAllViews()

		super.onDestroyView()
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
