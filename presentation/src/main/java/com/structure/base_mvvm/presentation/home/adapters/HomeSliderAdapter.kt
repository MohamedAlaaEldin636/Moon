package com.structure.base_mvvm.presentation.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.smarteist.autoimageslider.SliderViewAdapter
import com.structure.base_mvvm.domain.home.models.Slider
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.databinding.HomeSliderItemBinding
import com.structure.base_mvvm.presentation.home.viewModels.ItemSliderViewModel
import org.jetbrains.annotations.NotNull

class HomeSliderAdapter : SliderViewAdapter<HomeSliderAdapter.SliderAdapterVH>() {
  var sliderList: ArrayList<Slider> = ArrayList()
  override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
    val view =
      LayoutInflater.from(parent?.context).inflate(R.layout.home_slider_item, parent, false)
    return SliderAdapterVH(view)

  }

  override fun onBindViewHolder(holder: SliderAdapterVH, position: Int) {
    val data = sliderList[position]
    val itemViewModel = ItemSliderViewModel(data)
    holder.setViewModel(itemViewModel)

  }

  override fun getCount(): Int {
    return sliderList.size
  }

  fun update(@NotNull list: List<Slider>) {
    sliderList.clear()
    sliderList.addAll(list)
    notifyDataSetChanged()
  }

  class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
    private lateinit var itemLayoutBinding: HomeSliderItemBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemSliderViewModel) {
      itemLayoutBinding.itemViewModels = itemViewModel
    }
  }
}