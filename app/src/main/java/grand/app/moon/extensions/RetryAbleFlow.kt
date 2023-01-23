package grand.app.moon.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

fun ViewModel.aa() {

}

class RetryAbleFlow<T>(
	fragment: Fragment,
	private val getFlow: () -> Flow<T>,
	private val collectLatestAction: suspend (value: T) -> Unit,
	private val onRetry: () -> Unit
) {

	private var job: Job? = null

	private var value: Flow<T> = getFlow()

	private var weakRefFragment = WeakReference(fragment)

	fun collectLatest() {
		job?.cancel()
		job = null

		val fragment = weakRefFragment.get() ?: return

		job = kotlin.runCatching {
			fragment.viewLifecycleOwner.lifecycleScope.launch {
				fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					value.collectLatest(collectLatestAction)
				}
			}
		}.getOrNull()
	}

	fun retry() {
		job?.cancel()
		job = null

		value = getFlow()

		collectLatest()

		onRetry()
	}

	//public suspend fun <T> Flow<T>.collectLatest(action: suspend (value: T) -> Unit) {

}
