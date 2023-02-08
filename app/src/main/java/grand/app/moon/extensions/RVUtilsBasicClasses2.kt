@file:Suppress("OPT_IN_USAGE")

package grand.app.moon.extensions

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.exoplayer2.ExoPlayer
import grand.app.moon.core.extenstions.layoutInflater
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import java.lang.ref.WeakReference

class VHPagingItemCommonListUsageWithExoPlayer<VDB : ViewDataBinding, Item : Any>(
	private val adapter: RVPagingItemCommonListUsageWithExoPlayer<VDB, Item>,
	val binding: VDB,
	private val onBind: (binding: VDB, position: Int, item: Item, viewHolder: VHPagingItemCommonListUsageWithExoPlayer<VDB, Item>, adapter: RVPagingItemCommonListUsageWithExoPlayer<VDB, Item>) -> Unit,
	private val onItemClick: ((adapter: RVPagingItemCommonListUsageWithExoPlayer<VDB, Item>, binding: VDB) -> Unit)? = null,
	additionalListenersSetups: ((adapter: RVPagingItemCommonListUsageWithExoPlayer<VDB, Item>, binding: VDB) -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root) {

	var player: ExoPlayer? = null

	init {
		if (onItemClick != null) {
			binding.root.setOnClickListener {
				onItemClick.invoke(adapter, binding)
			}
		}

		additionalListenersSetups?.invoke(adapter, binding)
	}

	fun bind(position: Int, item: Item) {
		onBind(binding, position, item, this, adapter)
	}

	fun releasePlayer() {
		player?.release()
		player = null
	}

}

class RVPagingItemCommonListUsageWithExoPlayer<VDB : ViewDataBinding, Item : Any>(
	@LayoutRes private val layoutRes: Int,
	areItemsTheSameComparison: (oldItem: Item, newItem: Item) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	areContentsTheSameComparison: (oldItem: Item, newItem: Item) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	private val onViewRecycledAction: (VHPagingItemCommonListUsageWithExoPlayer<VDB, Item>) -> Unit = {},
	private val onItemClick: ((adapter: RVPagingItemCommonListUsageWithExoPlayer<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val additionalListenersSetups: ((adapter: RVPagingItemCommonListUsageWithExoPlayer<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val onBind: (binding: VDB, position: Int, item: Item, viewHolder: VHPagingItemCommonListUsageWithExoPlayer<VDB, Item>, adapter: RVPagingItemCommonListUsageWithExoPlayer<VDB, Item>) -> Unit,
) : PagingDataAdapter<Item, VHPagingItemCommonListUsageWithExoPlayer<VDB, Item>>(
	object : DiffUtil.ItemCallback<Item>() {
		override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
			areItemsTheSameComparison(oldItem, newItem)

		override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
			areContentsTheSameComparison(oldItem, newItem)
	}
) {

	val showEmptyViewFlow get() = loadStateFlow.mapLatest { loadState ->
		loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached &&
			snapshot().isEmpty()
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): VHPagingItemCommonListUsageWithExoPlayer<VDB, Item> {
		return VHPagingItemCommonListUsageWithExoPlayer(
			this,
			DataBindingUtil.inflate(parent.context.layoutInflater, layoutRes, parent, false),
			onBind,
			onItemClick,
			additionalListenersSetups,
		)
	}

	override fun onBindViewHolder(holder: VHPagingItemCommonListUsageWithExoPlayer<VDB, Item>, position: Int) {
		holder.bind(position, getItem(position) ?: return)
	}

	override fun onViewRecycled(holder: VHPagingItemCommonListUsageWithExoPlayer<VDB, Item>) {
		holder.releasePlayer()

		onViewRecycledAction(holder)

		super.onViewRecycled(holder)
	}

	fun releaseAllPlayers(recyclerView: RecyclerView) {
		//if (recyclerView.adapter != this) return

		for (index in 0 until recyclerView.childCount) {
			val child = recyclerView.getChildAt(index) ?: continue
			val viewHolder = recyclerView.getChildViewHolder(child)
			if (viewHolder is VHPagingItemCommonListUsageWithExoPlayer<*, *>) {
				viewHolder.releasePlayer()
			}
		}
	}


}
