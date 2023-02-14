package grand.app.moon.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.map
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.FragmentAllStoriesBinding
import grand.app.moon.extensions.app
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.setupWithRVItemCommonListUsage
import grand.app.moon.helpers.paging.withDefaultHeaderAndFooterAdapters
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.AllStoriesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class AllStoriesFragment : BaseFragment<FragmentAllStoriesBinding>() {

	private val viewModel by viewModels<AllStoriesViewModel>()

	private val dpToPx4 by lazy {
		context?.dpToPx(4f)?.roundToInt().orZero()
	}
	private val dpToPx6AndHalf by lazy {
		context?.dpToPx(4f)?.roundToInt().orZero()
	}
	private val dpToPx188 by lazy {
		context?.dpToPx(188f)?.roundToInt().orZero()
	}

	private val dpToPx119 by lazy {
		context?.dpToPx(119f)?.roundToInt().orZero()
	}

	override fun getLayoutId(): Int = R.layout.fragment_all_stories

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerViewFollowing.setupWithRVItemCommonListUsage(
			viewModel.adapterFollowing.withDefaultHeaderAndFooterAdapters(),
			true,
			1
		) { layoutParams ->
			layoutParams.topMargin = dpToPx4
			layoutParams.bottomMargin = dpToPx4
			layoutParams.leftMargin = dpToPx4
			layoutParams.rightMargin = dpToPx4

			val number = 4
			val itemMargins = dpToPx4 * 2

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			val width = (totalWidth - itemMargins) / number

			layoutParams.width = width
			layoutParams.height = dpToPx119
		}

		binding.recyclerViewOther.setupWithRVItemCommonListUsage(
			viewModel.adapterOther.withDefaultHeaderAndFooterAdapters(),
			false,
			2
		) { layoutParams ->
			layoutParams.topMargin = dpToPx6AndHalf
			layoutParams.bottomMargin = dpToPx6AndHalf
			layoutParams.leftMargin = dpToPx6AndHalf
			layoutParams.rightMargin = dpToPx6AndHalf

			layoutParams.width = (width / 2) - (dpToPx6AndHalf * 4)
			layoutParams.height = dpToPx188
		}

		binding.recyclerViewOther

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				launch {
					viewModel.storiesFollowing.collectLatest {
						viewModel.adapterFollowing.submitData(it)
					}
				}

				launch {
					viewModel.storiesOther.collectLatest {
						viewModel.adapterOther.submitData(it)
					}
				}

				launch {
					viewModel.adapterFollowing.showEmptyViewFlow.collectLatest {
						viewModel.showFollowingStories.value = it.not()
					}
				}
			}
		}
	}

}
