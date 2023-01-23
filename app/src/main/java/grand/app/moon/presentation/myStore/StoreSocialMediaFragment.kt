package grand.app.moon.presentation.myStore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentStoreSocialMediaBinding
import grand.app.moon.extensions.handleRetryAbleActionOrGoBackNullable
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myStore.viewModel.StoreSocialMediaViewModel

@AndroidEntryPoint
class StoreSocialMediaFragment : BaseFragment<FragmentStoreSocialMediaBinding>() {

	private val viewModel by viewModels<StoreSocialMediaViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		handleRetryAbleActionOrGoBackNullable(
			action = {
				viewModel.repoShop.getSocialMedia()
			}
		) {
			viewModel.adapter.submitList(it.orEmpty())
		}
	}

	override fun getLayoutId(): Int = R.layout.fragment_store_social_media

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
