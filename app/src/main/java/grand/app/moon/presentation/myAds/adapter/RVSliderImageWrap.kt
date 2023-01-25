package grand.app.moon.presentation.myAds.adapter

import android.view.ViewGroup
import com.smarteist.autoimageslider.SliderViewAdapter

class RVSliderImageWrap(
	private var list: List<String> = emptyList()
) : SliderViewAdapter<VHSliderImageWrap>() {

	override fun getCount(): Int = list.size

	override fun onCreateViewHolder(parent: ViewGroup): VHSliderImageWrap {
		return VHSliderImageWrap(parent)
	}

	override fun onBindViewHolder(holder: VHSliderImageWrap, position: Int) {
		holder.bind(list[position])
	}

	fun submitList(list: List<String>) {
		this.list = list
		notifyDataSetChanged()
	}

}
