package grand.app.moon.presentation.categories.adapter

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.NavDeepLinkRequest
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import grand.app.moon.R
import grand.app.moon.core.extenstions.inflateLayout
import grand.app.moon.databinding.ItemIconTextArrowBinding
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.categories.entity.ItemRelatedToCategories
import grand.app.moon.domain.categories.entity.ItemSubCategory
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrEmptyBA
import grand.app.moon.extensions.fromJsonInlinedOrNull
import grand.app.moon.extensions.orZero
import grand.app.moon.extensions.toJsonInlinedOrNull
import grand.app.moon.extensions.toJsonOrNull

class VHItemIconTextArrow(parent: ViewGroup, private val onItemClick: (view: View, id: Int, name: String) -> Unit) : RecyclerView.ViewHolder(
	parent.context.inflateLayout(R.id.item_icon_text_arrow, parent)
) {

	private val binding = ItemIconTextArrowBinding.bind(itemView)

	init {
		binding.constraintLayout.setOnClickListener {
			val item = (binding.constraintLayout.tag as? String)
				.fromJsonInlinedOrNull<ItemRelatedToCategories>() ?: return@setOnClickListener
			val name = binding.textView.text?.toString() ?: return@setOnClickListener

			val (id, isMainCategory) = when (item) {
				is ItemCategory -> item.id.orZero() to true
				is ItemSubCategory -> item.id.orZero() to false
			}

			onItemClick(binding.root, id, name)
		}
	}

	fun bind(item: ItemRelatedToCategories) {
		binding.constraintLayout.tag = item.toJsonInlinedOrNull()

		val (text, image) = when (item) {
			is ItemCategory -> item.name to item.image
			is ItemSubCategory -> item.name to item.image
		}

		binding.textView.text = text
		binding.imageImageView.setupWithGlideOrEmptyBA(image)
	}

}
