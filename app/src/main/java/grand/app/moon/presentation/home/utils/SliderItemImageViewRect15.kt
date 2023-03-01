package grand.app.moon.presentation.home.utils

import android.view.ViewGroup
import androidx.navigation.findNavController
import com.smarteist.autoimageslider.SliderViewAdapter
import grand.app.moon.R
import grand.app.moon.core.extenstions.inflateLayout
import grand.app.moon.databinding.ItemImageViewRect15Binding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import java.lang.ref.WeakReference

class RVSliderItemImageViewRect15(
	userLocalUseCase: UserLocalUseCase,
	var list: List<ItemAdvertisementInResponseHome> = emptyList(),
) : SliderViewAdapter<VHSliderItemImageViewRect15>() {

	private val weakRefUserLocalUseCase = WeakReference(userLocalUseCase)

	override fun getCount(): Int = list.size

	override fun onCreateViewHolder(parent: ViewGroup): VHSliderItemImageViewRect15 {
		return VHSliderItemImageViewRect15(parent, weakRefUserLocalUseCase)
	}

	override fun onBindViewHolder(holder: VHSliderItemImageViewRect15, position: Int) {
		holder.bind(list[position])
	}

	fun submitList(list: List<ItemAdvertisementInResponseHome>) {
		this.list = list
		notifyDataSetChanged()
	}

}

class VHSliderItemImageViewRect15(
	parent: ViewGroup,
	private val weakRefUserLocalUseCase: WeakReference<UserLocalUseCase>,
) : SliderViewAdapter.ViewHolder(
	parent.context.inflateLayout(R.layout.item_image_view_rect_15, parent)
) {

	private val binding = ItemImageViewRect15Binding.bind(itemView)

	init {
		binding.constraintLayout.setOnClickListener { view ->
			val context = view.context ?: return@setOnClickListener

			val navController = view.findNavController()

			val item = binding.constraintLayout.getTagJson<ItemAdvertisementInResponseHome>()
				?: return@setOnClickListener

			weakRefUserLocalUseCase.get()?.goToAdvDetailsCheckIfMyAdv(
				context,
				navController,
				item
			)
		}
	}

	fun bind(item: ItemAdvertisementInResponseHome) {
		binding.constraintLayout.setTagJson(item)

		binding.imageView.setupWithGlide {
			load(item.image).saveDiskCacheStrategyAll()
		}
	}

}
