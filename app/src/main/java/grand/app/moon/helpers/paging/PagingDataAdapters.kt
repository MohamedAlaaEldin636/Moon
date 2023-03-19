package grand.app.moon.helpers.paging

import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter

fun PagingDataAdapter<*, *>.withDefaultHeaderOnlyAdapter(): ConcatAdapter {
	val headerAdapter = LSAdapterLoadingErrorEmpty(this, false)

	return withCustomHeaderOnlyAdapter(
		headerAdapter,
	)
}

fun PagingDataAdapter<*, *>.withCustomHeaderOnlyAdapter(
	header: LoadStateAdapter<*>,
): ConcatAdapter {
	addLoadStateListener { loadStates ->
		header.loadState = if (loadStates.refresh is LoadState.Loading) {
			LoadState.Loading
		}else {
			loadStates.prepend
		}
	}

	return ConcatAdapter(header, this)
}

fun PagingDataAdapter<*, *>.withDefaultHeaderAndFooterAdapters(): ConcatAdapter {
	val headerAdapter = LSAdapterLoadingErrorEmpty(this, false)
	val footerAdapter = LSAdapterLoadingErrorEmpty(this, true)

	return withCustomAdapters(
		headerAdapter,
		footerAdapter
	)
}

/**
 * - Used since I wanna show loading on launch isa.
 *
 * - No need for prepend to check refresh error as if it happens on launch only append will be
 * notified so no 2 errors above each other will happen isa.
 */
fun PagingDataAdapter<*, *>.withCustomAdapters(
	header: LoadStateAdapter<*>,
	footer: LoadStateAdapter<*>
): ConcatAdapter {
	addLoadStateListener { loadStates ->
		header.loadState = if (loadStates.refresh is LoadState.Loading) {
			LoadState.Loading
		}else {
			loadStates.prepend
		}
		footer.loadState = when (loadStates.refresh) {
			is LoadState.Loading -> {
				LoadState.NotLoading(false)
			}
			is LoadState.Error -> {
				loadStates.refresh
			}
			else -> {
				loadStates.append
			}
		}
	}
	
	return ConcatAdapter(header, this, footer)
}

fun PagingDataAdapter<*, *>.withDefaultFooterOnlyAdapter(): ConcatAdapter {
	val footerAdapter = LSAdapterLoadingErrorEmpty(this, true)

	return withCustomAdaptersFooterOnly(
		footerAdapter
	)
}
fun PagingDataAdapter<*, *>.withCustomAdaptersFooterOnly(
	footer: LoadStateAdapter<*>
): ConcatAdapter {
	addLoadStateListener { loadStates ->
		footer.loadState = when (loadStates.refresh) {
			is LoadState.Loading -> {
				LoadState.NotLoading(false)
			}
			is LoadState.Error -> {
				loadStates.refresh
			}
			else -> {
				loadStates.append
			}
		}
	}

	return ConcatAdapter(this, footer)
}
