@file:Suppress("OPT_IN_USAGE")

package grand.app.moon.extensions

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
import grand.app.moon.core.extenstions.layoutInflater
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

fun RecyclerView.setupWithRVItemCommonListUsage(
	adapter: RecyclerView.Adapter<*>/*RVItemCommonListUsage<VDB, Item>*/,
	isHorizontalNotVertical: Boolean,
	spanCount: Int,
	onLayoutManager: LayoutManager.(RecyclerView.LayoutParams) -> Unit = {},
) {
	layoutManager = if (spanCount == 1) {
		object : LinearLayoutManager(
			context,
			if (isHorizontalNotVertical) HORIZONTAL else VERTICAL,
			false
		) {
			override fun checkLayoutParams(layoutParams: RecyclerView.LayoutParams?): Boolean {
				if (layoutParams != null) {
					onLayoutManager(layoutParams)
				}

				return super.checkLayoutParams(layoutParams)
			}
		}
	}else {
		object : GridLayoutManager(
			context,
			spanCount,
			if (isHorizontalNotVertical) HORIZONTAL else VERTICAL,
			false
		) {
			override fun checkLayoutParams(layoutParams: RecyclerView.LayoutParams?): Boolean {
				if (layoutParams != null) {
					onLayoutManager(layoutParams)
				}

				return super.checkLayoutParams(layoutParams)
			}
		}
	}

	this.adapter = adapter
}

class RVItemCommonListUsage<VDB : ViewDataBinding, Item : Any>(
	@LayoutRes private val layoutRes: Int,
	list: List<Item> = emptyList(),
	private val onItemClick: ((adapter: RVItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val additionalListenersSetups: ((adapter: RVItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val onBind: (binding: VDB, position: Int, item: Item) -> Unit,
) : RecyclerView.Adapter<VHItemCommonListUsage<VDB, Item>>() {

	var list = list
		private set

	override fun getItemCount(): Int = list.size

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): VHItemCommonListUsage<VDB, Item> {
		return VHItemCommonListUsage(
			this,
			DataBindingUtil.inflate(parent.context.layoutInflater, layoutRes, parent, false),
			onBind,
			onItemClick,
			additionalListenersSetups
		)
	}

	override fun onBindViewHolder(holder: VHItemCommonListUsage<VDB, Item>, position: Int) {
		holder.bind(position, list[position])
	}

	fun submitList(list: List<Item>) {
		this.list = list
		notifyDataSetChanged()
	}

	fun insertList(list: List<Item>) {
		if (list.isEmpty()) return
		if (this.list.isEmpty()) return submitList(list)

		val start = this.list.size
		this.list = this.list + list
		notifyItemRangeInserted(start, list.size)
	}

	fun deleteAt(index: Int) {
		if (index >= list.size) return

		list = list.toMutableList().also {
			it.removeAt(index)
		}.toList()

		notifyItemRemoved(index)
	}

}

open class RVPagingItemCommonListUsage<VDB : ViewDataBinding, Item : Any>(
	@LayoutRes private val layoutRes: Int,
	areItemsTheSameComparison: (oldItem: Item, newItem: Item) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	areContentsTheSameComparison: (oldItem: Item, newItem: Item) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	private val onItemClick: ((adapter: RVPagingItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val additionalListenersSetups: ((adapter: RVPagingItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val onBind: (binding: VDB, position: Int, item: Item) -> Unit,
) : PagingDataAdapter<Item, VHPagingItemCommonListUsage<VDB, Item>>(
	object : DiffUtil.ItemCallback<Item>() {
		override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
			areItemsTheSameComparison(oldItem, newItem)

		override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
			areContentsTheSameComparison(oldItem, newItem)
	}
) {

	/*
	viewModel.adapter.loadStateFlow.collectLatest { loadState ->
                        if (loadState/*.source*/.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                            viewModel.showEmptyView.value = viewModel.adapter.snapshot().isEmpty()
                        }
                    }
	 */
	val showEmptyViewFlow get() = loadStateFlow.mapLatest { loadState ->
		loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached &&
			snapshot().isEmpty()
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): VHPagingItemCommonListUsage<VDB, Item> {
		return VHPagingItemCommonListUsage(
			this,
			DataBindingUtil.inflate(parent.context.layoutInflater, layoutRes, parent, false),
			onBind,
			onItemClick,
			additionalListenersSetups
		)
	}

	override fun onBindViewHolder(holder: VHPagingItemCommonListUsage<VDB, Item>, position: Int) {
		holder.bind(position, getItem(position) ?: return)
	}

}

class VHPagingItemCommonListUsage<VDB : ViewDataBinding, Item : Any>(
	private val adapter: RVPagingItemCommonListUsage<VDB, Item>,
	private val binding: VDB,
	private val onBind: (binding: VDB, position: Int, item: Item) -> Unit,
	private val onItemClick: ((adapter: RVPagingItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
	additionalListenersSetups: ((adapter: RVPagingItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root) {

	init {
		if (onItemClick != null) {
			binding.root.setOnClickListener {
				onItemClick.invoke(adapter, binding)
			}
		}

		additionalListenersSetups?.invoke(adapter, binding)
	}

	fun bind(position: Int, item: Item) {
		onBind(binding, position, item)
	}

}

class VHItemCommonListUsage<VDB : ViewDataBinding, Item : Any>(
	private val adapter: RVItemCommonListUsage<VDB, Item>,
	private val binding: VDB,
	private val onBind: (binding: VDB, position: Int, item: Item) -> Unit,
	private val onItemClick: ((adapter: RVItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
	additionalListenersSetups: ((adapter: RVItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
) : RecyclerView.ViewHolder(binding.root) {

	init {
		if (onItemClick != null) {
			binding.root.setOnClickListener {
				onItemClick.invoke(adapter, binding)
			}
		}

		additionalListenersSetups?.invoke(adapter, binding)
	}

	fun bind(position: Int, item: Item) {
		onBind(binding, position, item)
	}

}

/*
	init {
		binding.constraintLayout.setOnClickListener {
			val id = binding.constraintLayout.tag as? Int ?: return@setOnClickListener
			val name = binding.textView.text?.toString() ?: return@setOnClickListener

			onItemClick(binding.root, id, name)
		}
	}

	fun bind(item: ItemRelatedToCategories) {
		binding.constraintLayout.tag = when (item) {
			is ItemCategory -> item.id.orZero()
			is ItemSubCategory -> item.id.orZero()
		}

		val (text, image) = when (item) {
			is ItemCategory -> item.name to item.image
			is ItemSubCategory -> item.name to item.image
		}

		binding.textView.text = text
		binding.imageImageView.isVisible = image.isNullOrEmpty().not()
		binding.imageImageView.setupWithGlideOrEmptyBA(image)
	}

}
class RVItemIconTextArrow(
	private val onItemClick: (view: View, id: Int, name: String, subcategories: List<ItemSubCategory>, brands: List<ItemSubCategory>) -> Unit
) : RecyclerView.Adapter<VHItemIconTextArrow>() {

	private var list = emptyList<ItemRelatedToCategories>()

	override fun getItemCount(): Int = list.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemIconTextArrow {
		return VHItemIconTextArrow(parent) { view, id, name ->
			val subcategories = list.filterIsInstance<ItemCategory>().firstOrNull { it.id == id }
				?.subCategories.orEmpty()

			val brands = list.filterIsInstance<ItemCategory>().firstOrNull { it.id == id }
				?.brands.orEmpty()

			onItemClick(view, id, name, subcategories, brands)
		}
	}

	override fun onBindViewHolder(holder: VHItemIconTextArrow, position: Int) {
		holder.bind(list[position])
	}

	fun submitList(list: List<ItemRelatedToCategories>) {
		this.list = list
		notifyDataSetChanged()
	}

}

 */
