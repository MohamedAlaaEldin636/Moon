package grand.app.moon.presentation.categories.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.categories.entity.ItemRelatedToCategories
import grand.app.moon.domain.categories.entity.ItemSubCategory

class RVItemIconTextArrow(
	private val onItemClick: (view: View, id: Int, name: String, subcategories: List<ItemSubCategory>) -> Unit
) : RecyclerView.Adapter<VHItemIconTextArrow>() {

	private var list = emptyList<ItemRelatedToCategories>()

	override fun getItemCount(): Int = list.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemIconTextArrow {
		return VHItemIconTextArrow(parent) { view, id, name ->
			val subcategories = list.filterIsInstance<ItemCategory>().firstOrNull { it.id == id }
				?.subCategories.orEmpty()

			onItemClick(view, id, name, subcategories)
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
