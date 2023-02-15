package grand.app.moon.presentation.myAds.adapter

import android.view.ViewGroup
import android.widget.ImageView
import com.smarteist.autoimageslider.SliderViewAdapter
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrSplashBA

class RVSliderImageFull(
	private var list: List<String> = emptyList(),
	private val loadImageAction: ImageView.(item: String) -> Unit = { setupWithGlideOrSplashBA(it) }
) : SliderViewAdapter<VHSliderImageFull>() {

	override fun getCount(): Int = list.size

	override fun onCreateViewHolder(parent: ViewGroup): VHSliderImageFull {
		return VHSliderImageFull(parent, loadImageAction)
	}

	override fun onBindViewHolder(holder: VHSliderImageFull, position: Int) {
		holder.bind(list[position])
	}

	fun submitList(list: List<String>) {
		this.list = list
		notifyDataSetChanged()
	}

}
