package codes.grand.images_slider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.grand.images_slider.ImagesSliderAdapter.ImagesSliderViewHolder
import codes.grand.images_slider.utils.loadSliderImage
import grand.images_slider.R
import grand.images_slider.databinding.ItemSliderImageBinding

internal class ImagesSliderAdapter(private var itemClick: ((position: Int) -> Unit)) :
  ListAdapter<String, ImagesSliderViewHolder>(DIFF_CALLBACK) {

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
      override
      fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem.hashCode() == newItem.hashCode()

      override
      fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
    }
  }

  override
  fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ImagesSliderViewHolder {
    val root = LayoutInflater.from(parent.context).inflate(R.layout.item_slider_image, parent, false)
    return ImagesSliderViewHolder(ItemSliderImageBinding.bind(root))
  }

  override
  fun onBindViewHolder(holder: ImagesSliderViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ImagesSliderViewHolder(private val itemBinding: ItemSliderImageBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var currentItem: String

    init {
      itemBinding.item.setOnClickListener {
        itemClick.invoke(currentList.indexOf(currentItem))
      }
    }

    fun bind(item: String) {
      currentItem = item

      displayImage()
    }

    private fun displayImage() {
      itemBinding.ivImage.loadSliderImage(currentItem)
    }
  }
}