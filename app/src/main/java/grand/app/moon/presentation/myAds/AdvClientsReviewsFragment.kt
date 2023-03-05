package grand.app.moon.presentation.myAds

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentAdvClientsReviewsBinding
import grand.app.moon.extensions.*
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myAds.viewModel.AdvClientsReviewsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdvClientsReviewsFragment : BaseFragment<FragmentAdvClientsReviewsBinding>() {

	private val viewModel by viewModels<AdvClientsReviewsViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBack(
			action = {
				if (viewModel.args.forAdNotStore) {
					viewModel.repoShop.getReviewsForAdv(viewModel.args.advId)
				}else {
					viewModel.repoShop.getReviewsForStore(viewModel.args.advId)
				}
			}
		) {
			viewModel.response.value = it
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_adv_clients_reviews

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

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.reviews.collectLatest {
					viewModel.adapter.submitData(it)
				}
			}
		}

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<Boolean> {
			viewModel.adapter.refresh()

			findNavController().setResultInPreviousBackStackEntrySavedStateHandleViaGson(true, AppConsts.KEY_BOOLEAN_1)
		}
	}

}
