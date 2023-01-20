package grand.app.moon.presentation.myStore

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.postDelayed
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentStoreFullDataBinding
import grand.app.moon.extensions.handleRetryAbleActionCancellable
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myStore.viewModel.StoreFullDataViewModel

@AndroidEntryPoint
class StoreFullDataFragment : BaseFragment<FragmentStoreFullDataBinding>() {

	private val viewModel by viewModels<StoreFullDataViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// TO-DO If later required always load when enter another fragment then make all fragments entered
		// from here call setPrevResult to true and observe here and if tru re-call api isa.
		handleRetryAbleActionCancellable(
			action = {
				viewModel.repoPackages.getShopInfo()
			}
		) { response ->
			viewModel.response.value = response

			val list = viewModel.list.map {
				when (it.imageDrawableRes) {
					R.drawable.ic_store_data_4 -> it.copy(notComplete = response.storeInfo.orFalse().not())
					R.drawable.ic_category_4 -> it.copy(notComplete = response.categories.orFalse().not())
					R.drawable.ic_sub_category_4 -> it.copy(notComplete = response.subCategories.orFalse().not())
					R.drawable.ic_working_hours_4 -> it.copy(notComplete = response.workingHours.orFalse().not())
					R.drawable.ic_social_4 -> it.copy(notComplete = response.socialMediaLinks.orFalse().not())
					else -> it.copy()
				}
			}
			viewModel.adapter.submitList(list)
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_store_full_data

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			false,
			1
		)
	}

}
