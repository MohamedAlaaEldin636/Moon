package grand.app.moon.presentation.myAds.adapter

import android.view.ViewGroup
import com.smarteist.autoimageslider.SliderViewAdapter

class RVSliderImageFull : SliderViewAdapter<VHSliderImageFull>() {

	private var list = emptyList<String>()

	override fun getCount(): Int = list.size

	override fun onCreateViewHolder(parent: ViewGroup): VHSliderImageFull {
		return VHSliderImageFull(parent)
	}

	override fun onBindViewHolder(holder: VHSliderImageFull, position: Int) {
		holder.bind(list[position])
	}

	fun submitList(list: List<String>) {
		this.list = list
		notifyDataSetChanged()
	}

}
