package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentSimpleUserListOfInteractionsBinding
import grand.app.moon.extensions.navigateDeepLinkWithOptions
import grand.app.moon.extensions.orStringNullIfNullOrEmpty
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.extensions.toStringOrEmpty
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.SimpleUserListOfInteractionsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SimpleUserListOfInteractionsFragment : BaseFragment<FragmentSimpleUserListOfInteractionsBinding>() {

	companion object {
		fun launch(navController: NavController, screenTitle: String, dataTitle: String, type: Type, id: Int) {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.simple.user.list.of.interactions",
				paths = arrayOf(
					screenTitle.orStringNullIfNullOrEmpty(),
					dataTitle.orStringNullIfNullOrEmpty(),
					type.toString(),
					id.toStringOrEmpty().orStringNullIfNullOrEmpty()
				)
			)
		}
	}

	private val viewModel by viewModels<SimpleUserListOfInteractionsViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_simple_user_list_of_interactions

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
				viewModel.users.collectLatest {
					viewModel.adapter.submitData(it)
				}
			}
		}
	}

	enum class Type {
		EXPLORE_LIKES, STORE_VIEWS, STORE_FOLLOWERS,
		SHOP_INFO_STORY_LIKES,
		SHOP_INFO_STORY_VIEWS,
		SHOP_INFO_STORY_SHARES,
		SHOP_INFO_EXPLORE_LIKES,
		SHOP_INFO_EXPLORE_COMMENTS,
		SHOP_INFO_EXPLORE_SHARES,
	}

	data class Item(
		var id: Int,
		var name: String,
		var image: String,
		var count: Int? = null,
		var createdAt: String? = null,
		var email: String? = null,
		var phone: String? = null,
		var countryCode: String? = null,
	)

}
