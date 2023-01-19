package grand.app.moon.presentation.myAds

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentMyAdsBinding
import grand.app.moon.extensions.indexOfFirstOrNull
import grand.app.moon.extensions.observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myAds.viewModel.MyAdsViewModel

@AndroidEntryPoint
class MyAdsFragment : BaseFragment<FragmentMyAdsBinding>()  {

	private val viewModel by viewModels<MyAdsViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_my_ads

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel.searchNow(this)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

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
