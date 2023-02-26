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
	/** Ex. layoutParams.width = width / 3 */
	onGridLayoutSpanSizeLookup: (GridLayoutManager.(Int) -> Int)? = null,
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
		}.also { gridLayoutManager ->
			if (onGridLayoutSpanSizeLookup != null) {
				gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
					override fun getSpanSize(position: Int): Int {
						return gridLayoutManager.onGridLayoutSpanSizeLookup(position)
					}
				}
			}
		}
	}

	this.adapter = adapter
}

open class RVItemCommonListUsage<VDB : ViewDataBinding, Item : Any>(
	@LayoutRes private val layoutRes: Int,
	list: List<Item> = emptyList(),
	private val onItemClick: ((adapter: RVItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val additionalListenersSetups: ((adapter: RVItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val afterOnCreateViewHolder: ((viewHolder: VHItemCommonListUsage<VDB, Item>, layoutRes: Int) -> Unit)? = null,
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
		).also {
			afterOnCreateViewHolder?.invoke(it, viewType)
		}
	}

	override fun onBindViewHolder(holder: VHItemCommonListUsage<VDB, Item>, position: Int) {
		holder.bind(position, list[position])
	}

	fun submitList(list: List<Item>) {
		this.list = list
		notifyDataSetChanged()
	}

	fun updateItem(position: Int, item: Item) {
		this.list = this.list.toMutableList().also {
			it[position] = item
		}.toList()
		notifyItemChanged(position)
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

abstract class RVItemCommonListUsage2<VDB : ViewDataBinding, Item : Any>(
	@LayoutRes private val layoutRes: Int,
	list: List<Item> = emptyList(),
	private val onItemClick: ((adapter: RVItemCommonListUsage2<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val additionalListenersSetups: ((adapter: RVItemCommonListUsage2<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val afterOnCreateViewHolder: ((viewHolder: VHItemCommonListUsage2<VDB, Item>, layoutRes: Int) -> Unit)? = null,
) : RecyclerView.Adapter<VHItemCommonListUsage2<VDB, Item>>() {

	var list = list
		private set

	abstract fun onBind(binding: VDB, position: Int, item: Item)

	override fun getItemCount(): Int = list.size

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): VHItemCommonListUsage2<VDB, Item> {
		return VHItemCommonListUsage2(
			this,
			DataBindingUtil.inflate(parent.context.layoutInflater, layoutRes, parent, false),
			onItemClick,
			additionalListenersSetups
		).also {
			afterOnCreateViewHolder?.invoke(it, viewType)
		}
	}

	override fun onBindViewHolder(holder: VHItemCommonListUsage2<VDB, Item>, position: Int) {
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
	private val onViewRecycledAction: (VHPagingItemCommonListUsage<VDB, Item>) -> Unit = {},
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

	override fun onViewRecycled(holder: VHPagingItemCommonListUsage<VDB, Item>) {
		onViewRecycledAction(holder)

		super.onViewRecycled(holder)
	}

	fun updateItem(position: Int, adjustmentsAction: (Item) -> Unit) {
		adjustmentsAction(snapshot().items[position])
		notifyItemChanged(position)
	}

}

class VHPagingItemCommonListUsage<VDB : ViewDataBinding, Item : Any>(
	private val adapter: RVPagingItemCommonListUsage<VDB, Item>,
	val binding: VDB,
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
	val binding: VDB,
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

class VHItemCommonListUsage2<VDB : ViewDataBinding, Item : Any>(
	private val adapter: RVItemCommonListUsage2<VDB, Item>,
	val binding: VDB,
	private val onItemClick: ((adapter: RVItemCommonListUsage2<VDB, Item>, binding: VDB) -> Unit)? = null,
	additionalListenersSetups: ((adapter: RVItemCommonListUsage2<VDB, Item>, binding: VDB) -> Unit)? = null,
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
		adapter.onBind(binding, position, item)
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

class VHItemCommonListUsageWithDifferentItems<Item : Any>(
	private val adapter: RVItemCommonListUsageWithDifferentItems<Item>,
	val binding: ViewDataBinding,
	private val onBind: (binding: ViewDataBinding, position: Int, item: Item) -> Unit,
	private val onItemClick: ((adapter: RVItemCommonListUsageWithDifferentItems<Item>, binding: ViewDataBinding) -> Unit)? = null,
	additionalListenersSetups: ((adapter: RVItemCommonListUsageWithDifferentItems<Item>, binding: ViewDataBinding) -> Unit)? = null,
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

open class RVItemCommonListUsageWithDifferentItems<Item : Any>(
	private val getLayoutRes: RVItemCommonListUsageWithDifferentItems<Item>.(position: Int) -> Int,
	list: List<Item> = emptyList(),
	private val onItemClick: ((adapter: RVItemCommonListUsageWithDifferentItems<Item>, binding: ViewDataBinding) -> Unit)? = null,
	private val additionalListenersSetups: ((adapter: RVItemCommonListUsageWithDifferentItems<Item>, binding: ViewDataBinding) -> Unit)? = null,
	private val afterOnCreateViewHolder: ((viewHolder: VHItemCommonListUsageWithDifferentItems<Item>, layoutRes: Int, binding: ViewDataBinding) -> Unit)? = null,
	private val getItemIdBlock: ((position: Int) -> Long)? = null,
	private val onBind: (binding: ViewDataBinding, position: Int, item: Item) -> Unit,
) : RecyclerView.Adapter<VHItemCommonListUsageWithDifferentItems<Item>>() {

	var list = list
		private set

	override fun getItemCount(): Int = list.size

	override fun getItemId(position: Int): Long {
		return getItemIdBlock?.invoke(position) ?: super.getItemId(position)
	}

	override fun getItemViewType(position: Int): Int {
		return getLayoutRes(position)
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): VHItemCommonListUsageWithDifferentItems<Item> {
		val binding = DataBindingUtil.inflate<ViewDataBinding>(
			parent.context.layoutInflater, viewType, parent, false
		)

		return VHItemCommonListUsageWithDifferentItems(
			this,
			binding,
			onBind,
			onItemClick,
			additionalListenersSetups
		).also {
			afterOnCreateViewHolder?.invoke(it, viewType, binding)
		}
	}

	override fun onBindViewHolder(holder: VHItemCommonListUsageWithDifferentItems<Item>, position: Int) {
		holder.bind(position, list[position])
	}

	fun submitList(list: List<Item>) {
		this.list = list
		notifyDataSetChanged()
	}

	fun updateItem(position: Int, item: Item) {
		this.list = this.list.toMutableList().also {
			it[position] = item
		}.toList()
		notifyItemChanged(position)
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

