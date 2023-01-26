package grand.app.moon.presentation.myAds.adapter

import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.smarteist.autoimageslider.SliderViewAdapter
import grand.app.moon.R
import grand.app.moon.core.extenstions.inflateLayout
import grand.app.moon.databinding.ItemImageFullBinding
import grand.app.moon.databinding.ItemImageWrapBinding
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrSplashBA
import grand.app.moon.extensions.bindingAdapter.setupWithGlideWithDefaultsPlaceholderAndError

class VHSliderImageWrap(parent: ViewGroup) : SliderViewAdapter.ViewHolder(
	parent.context.inflateLayout(R.layout.item_image_wrap, parent)
) {

	private val binding = ItemImageWrapBinding.bind(itemView)

	fun bind(item: String) {
		binding.imageView.setupWithGlideWithDefaultsPlaceholderAndError(item)
	}

}
