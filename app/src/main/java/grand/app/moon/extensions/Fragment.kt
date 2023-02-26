package grand.app.moon.extensions

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.postDelayed
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun <reified T> Fragment.setFragmentResultListenerUsingJson(
	key: String,
	crossinline onResult: (value: T) -> Unit
) {
	setFragmentResultListener(key) { _, bundle ->
		val value = bundle.getString(AppConsts.NavController.GSON_KEY).fromJsonInlinedOrNull<T>()

		clearFragmentResultListener(key)

		if (value != null) {
			onResult(value)
		}
	}
}
inline fun <reified T> Fragment.setFragmentResultUsingJson(key: String, value: T) {
	setFragmentResult(
		key,
		bundleOf(AppConsts.NavController.GSON_KEY to value.toJsonInlinedOrNull().orEmpty())
	)
}

/*
fragment.setFragmentResult("aaa", Bundle.EMPTY)
		fragment.setFragmentResultListener("Aaa") { requestKey, bundle ->
			fragment.clearFragmentResultListener("aaa")
		}
 */

fun <T : Any> Fragment.getOnPageChangeForAdapterUsingViewLifecycle(
	adapter: PagingDataAdapter<T, *>,
	minimumPageNumber: Int = 1,
	onPageChange: (page: Int) -> Unit = {},
) {
	var list: List<T>? = null
	var page: Int? = null

	viewLifecycleOwner.lifecycleScope.launch {
		viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			adapter.loadStateFlow.collectLatest {
				val newList = adapter.snapshot().items

				if (newList != list && newList.isNotEmpty()) {
					list = newList

					page = if (page == null) minimumPageNumber else page
					onPageChange(page.orZero())
					page = page.orZero().inc()
				}
			}
		}
	}
}

fun <T : Any> Fragment.performListTransformationAndGetOnDistinctChangeForAdapterUsingViewLifecycle(
	adapter: PagingDataAdapter<T, *>,
	transformation: (List<T>) -> List<T> = { it },
	onChange: (newList: List<T>) -> Unit = {},
) {
	var list: List<T>? = null

	viewLifecycleOwner.lifecycleScope.launch {
		viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			adapter.loadStateFlow.collectLatest {
				MyLogger.e("checking transformations 1")

				val newList = adapter.snapshot().items.let(transformation)

				MyLogger.e("checking transformations 2")

				if (newList != list) {
					MyLogger.e("checking transformations 3")

					list = newList

					MyLogger.e("checking transformations 4")

					adapter.submitData(PagingData.from(list.orEmpty()))

					MyLogger.e("checking transformations 5")

					onChange(list.orEmpty())

					MyLogger.e("checking transformations 6")
				}
			}
		}
	}
}

fun Fragment.observeAdapterOnDataFirstLoadedOnceUsingViewLifecycle(
	adapter: PagingDataAdapter<*, *>,
	action: () -> Unit,
) {
	var job: Job? = null
	job = viewLifecycleOwner.lifecycleScope.launch {
		viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			adapter.loadStateFlow.collectLatest { state ->
				if (state.refresh is LoadState.NotLoading) {
					job?.cancel()
					job = null

					action()
				}
			}
		}
	}
}

fun Fragment.observeAdapterOnDataFirstLoadedOnceHaveDataUsingViewLifecycle(
	adapter: PagingDataAdapter<*, *>,
	action: () -> Unit,
) {
	var job: Job? = null
	job = viewLifecycleOwner.lifecycleScope.launch {
		viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			adapter.loadStateFlow.collectLatest {
				if (adapter.snapshot().items.isNotEmpty()) {
					job?.cancel()
					job = null

					action()
				}
			}
		}
	}
}

inline fun <reified F : Fragment> View.findFragmentOrNull(): F? {
	return try {
		findFragment()
	}catch (e: IllegalStateException) {
		null
	}
}
