package grand.app.moon.extensions

import android.view.View
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T : Any> Fragment.performListTransformationAndGetOnDistinctChangeForAdapterUsingViewLifecycle(
	adapter: PagingDataAdapter<T, *>,
	transformation: (List<T>) -> List<T> = { it },
	onChange: (newList: List<T>) -> Unit = {},
) {
	var list = emptyList<T>()

	viewLifecycleOwner.lifecycleScope.launch {
		viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			adapter.loadStateFlow.collectLatest {
				val newList = adapter.snapshot().items.let(transformation)

				if (newList != list) {
					list = newList

					adapter.submitData(PagingData.from(list))

					onChange(list)
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

inline fun <reified F : Fragment> View.findFragmentOrNull(): F? {
	return try {
		findFragment()
	}catch (e: IllegalStateException) {
		null
	}
}
