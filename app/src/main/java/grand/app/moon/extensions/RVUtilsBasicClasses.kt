package grand.app.moon.extensions

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.compose.ui.Alignment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.core.extenstions.layoutInflater

fun <VDB : ViewDataBinding, Item : Any> RecyclerView.setupWithRVItemCommonListUsage(
	adapter: RVItemCommonListUsage<VDB, Item>,
	isHorizontalNotVertical: Boolean,
	spanCount: Int,
) {
	layoutManager = if (spanCount == 1) {
		LinearLayoutManager(
			context,
			if (isHorizontalNotVertical) LinearLayoutManager.HORIZONTAL else LinearLayoutManager.VERTICAL,
			false
		)
	}else {
		GridLayoutManager(
			context,
			spanCount,
			if (isHorizontalNotVertical) GridLayoutManager.HORIZONTAL else GridLayoutManager.VERTICAL,
			false
		)
	}

	this.adapter = adapter
}

/*
direction - span(col/rows) - cell item xml - on click - additional listeners setups def none - onBind changes
accepts list - has change all submit list - bs kda el donya tmam isa. can start with the list as well
 */
class RVItemCommonListUsage<VDB : ViewDataBinding, Item : Any>(
	@LayoutRes private val layoutRes: Int,
	private var list: List<Item> = emptyList(),
	private val onItemClick: ((adapter: RVItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val additionalListenersSetups: ((adapter: RVItemCommonListUsage<VDB, Item>, binding: VDB) -> Unit)? = null,
	private val onBind: (binding: VDB, position: Int, item: Item) -> Unit,
) : RecyclerView.Adapter<VHItemCommonListUsage<VDB, Item>>() {

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
