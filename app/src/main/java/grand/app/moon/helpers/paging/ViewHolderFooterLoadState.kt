package grand.app.moon.helpers.paging

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import grand.app.moon.R
import grand.app.moon.databinding.ItemRetryAbleErrorBinding
import grand.app.moon.databinding.ItemText2Binding

sealed class ViewHolderFooterLoadState(
	parent: ViewGroup,
	@LayoutRes layoutRes: Int
) : RecyclerView.ViewHolder(
	parent.context.inflateLayout(layoutRes, parent, false)
) {
	
	companion object {
		
		fun getInstance(loadState: LoadState, parent: ViewGroup, errorRetry: () -> Unit): ViewHolderFooterLoadState {
			return when (loadState) {
				is LoadState.Error -> Error(parent, errorRetry)
				is LoadState.Loading -> Loading(parent)
				is LoadState.NotLoading -> NotLoading(parent)
			}
		}
		
	}
	
	class Loading(parent: ViewGroup) : ViewHolderFooterLoadState(parent, R.layout.item_loading)
	
	class Error(parent: ViewGroup, private val retry: () -> Unit) : ViewHolderFooterLoadState(parent, R.layout.item_retry_able_error) {
		
		private val binding = ItemRetryAbleErrorBinding.bind(itemView)
		
		init {
			binding.retryMaterialButton.setOnClickListener {
				retry()
			}
		}
		
		fun onBind(text: String) {
			binding.textView.text = text
		}
		
		fun onBind(@StringRes stringRes: Int) {
			binding.textView.text = binding.root.context.getString(stringRes)
		}
		
	}
	
	class NotLoading(parent: ViewGroup) : ViewHolderFooterLoadState(parent, R.layout.item_text_2) {
		
		private val binding = ItemText2Binding.bind(itemView)
		
		fun onBind(@StringRes stringRes: Int) {
			binding.textView.text = binding.root.context.getString(stringRes)
		}
	}
	
}
