package grand.app.moon.presentation.myAds.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import com.smarteist.autoimageslider.SliderViewAdapter
import grand.app.moon.R
import grand.app.moon.core.extenstions.inflateLayout
import grand.app.moon.databinding.ItemImageFullBinding
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrSplashBA

class VHSliderImageFull(
	parent: ViewGroup,
	private val loadImageAction: ImageView.(item: String) -> Unit
) : SliderViewAdapter.ViewHolder(
	parent.context.inflateLayout(R.layout.item_image_full, parent)
) {

	private val binding = ItemImageFullBinding.bind(itemView)

	fun bind(item: String) {
		binding.imageView.loadImageAction(item)
	}

}
