package grand.app.moon.presentation.home.utils

import android.view.ViewGroup
import android.widget.ImageView
import com.smarteist.autoimageslider.SliderViewAdapter
import grand.app.moon.R
import grand.app.moon.core.extenstions.inflateLayout
import grand.app.moon.databinding.ItemImageFullBinding
import grand.app.moon.databinding.ItemImageViewRect15Binding
import grand.app.moon.extensions.saveDiskCacheStrategyAll
import grand.app.moon.extensions.setupWithGlide

class VHSliderItemImageViewRect15(
	parent: ViewGroup,
) : SliderViewAdapter.ViewHolder(
	parent.context.inflateLayout(R.layout.item_image_view_rect_15, parent)
) {

	private val binding = ItemImageViewRect15Binding.bind(itemView)

	fun bind(item: String) {
		// todo set on click listener isa.

		binding.imageView.setupWithGlide {
			load(item).saveDiskCacheStrategyAll()
		}
	}

}
