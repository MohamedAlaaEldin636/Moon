package grand.app.moon.helpers.paging

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import grand.app.moon.R

/**
 * - Prefix LS denotes LoadState
 */
class LSAdapterLoadingErrorEmpty(
  private val adapter: PagingDataAdapter<*, *>,
  private val isFooter: Boolean,
  @StringRes private val emptyListMsgStringRes: Int = R.string.no_data_found,
  private val canShowEmptyView: Boolean = true,
) : LoadStateAdapter<ViewHolderFooterLoadState>() {
	
	override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
		return loadState is LoadState.Loading || loadState is LoadState.Error
			// used to show empty view.
			|| (canShowEmptyView && isFooter && loadState is LoadState.NotLoading && loadState.endOfPaginationReached && adapter.snapshot().isEmpty())
	}
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		loadState: LoadState
	): ViewHolderFooterLoadState {
		return ViewHolderFooterLoadState.getInstance(loadState, parent) {
			kotlin.runCatching { adapter.retry() }
		}
	}

  private  val TAG = "LSAdapterLoadingErrorEm"
	override fun onBindViewHolder(holder: ViewHolderFooterLoadState, loadState: LoadState) {
		when (holder) {
			is ViewHolderFooterLoadState.Error -> {
				val exception = ((loadState as? LoadState.Error)
					?.error as? MAPagingException)
				exception?.message?.also {
					Log.d(TAG,"Error -> $it\nThrowable -> $exception\nMAResult.Failure -> ${exception.failure}")

					holder.onBind(it)
				} ?: holder.onBind(R.string.something_went_wrong_please_try_again)
			}
			is ViewHolderFooterLoadState.NotLoading -> holder.onBind(emptyListMsgStringRes)
			is ViewHolderFooterLoadState.Loading -> { /* Do nothing */ }
		}
	}
	
}
