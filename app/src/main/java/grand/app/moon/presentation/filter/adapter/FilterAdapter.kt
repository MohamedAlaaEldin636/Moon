package grand.app.moon.presentation.filter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rizlee.rangeseekbar.RangeSeekBar
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.databinding.ItemFilterBinding
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.presentation.filter.FILTER_TYPE
import grand.app.moon.presentation.filter.viewModels.ItemFilterViewModel


class FilterAdapter : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
  lateinit var context: Context
  var position = -1
  var clickEvent: MutableLiveData<FilterProperty> = MutableLiveData()
  var baseList = ArrayList<FilterProperty>()


  private val differCallback = object : DiffUtil.ItemCallback<FilterProperty>() {
    override fun areItemsTheSame(
      oldItem: FilterProperty,
      newItem: FilterProperty
    ): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
      oldItem: FilterProperty,
      newItem: FilterProperty
    ): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_filter, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  private val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemFilterViewModel(data)
    holder.itemLayoutBinding.itemMore.itemFilterMore.setOnClickListener {
      this.position = position
      clickEvent.value = data
    }

    if(data.type == 3) {
      data.from = data.min.toString()
      data.to = data.max.toString()
    }
    holder.itemLayoutBinding.itemPrice.rangeSeekBar.listenerPost  = object : RangeSeekBar.OnRangeSeekBarPostListener,
      RangeSeekBar.OnRangeSeekBarRealTimeListener {
      override fun onValuesChanged(minValue: Float, maxValue: Float) {
      }

      override fun onValuesChanged(minValue: Int, maxValue: Int) {
        data.from = minValue.toString()
        data.to = maxValue.toString()
        holder.itemLayoutBinding.itemPrice.edtFrom.setText(data.from)
        holder.itemLayoutBinding.itemPrice.edtTo.setText(data.to)
      }

      override fun onValuesChanging(minValue: Float, maxValue: Float) {

      }

      override fun onValuesChanging(minValue: Int, maxValue: Int) {

      }
    }


//    holder.itemLayoutBinding.discreteRangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
//      override fun onStartTrackingTouch(slider: RangeSlider) {
//        // Responds to when slider's touch event is being started
//
//        Log.d(TAG, "onStartTrackingTouch: ${slider.values}")
//        Log.d(TAG, "onStartTrackingTouch: ${slider.valueTo}")
//      }
//
//      override fun onStopTrackingTouch(slider: RangeSlider) {
//        // Responds to when slider's touch event is being stopped
//        Log.d(TAG, "onStartTrackingTouch: ${slider.values}")
//
//      }
//    })

//    holder.itemLayoutBinding.rangeSeekBar.setOnRangeSeekBarChangeListener(object : OnRangeSeekBarChangeListener {
//      override fun onProgressChanged(
//        seekBar: RangeSeekBar, progressStart: Int, progressEnd: Int, fromUser: Boolean
//      ) {
//        data.from = progressStart.toString()
//        data.to = progressEnd.toString()
//      }
//
//      override fun onStartTrackingTouch(seekBar: RangeSeekBar) {}
//      override fun onStopTrackingTouch(seekBar: RangeSeekBar) {}
//    })

    holder.setViewModel(itemViewModel)

  }

  override fun getItemCount(): Int {
    return differ.currentList.size
  }

  override fun onViewAttachedToWindow(holder: ViewHolder) {
    super.onViewAttachedToWindow(holder)
    holder.bind()
  }

  override fun onViewDetachedFromWindow(holder: ViewHolder) {
    super.onViewDetachedFromWindow(holder)
    holder.unBind()
  }

  fun updateFilterSelected(property: FilterProperty) {
    Log.d(TAG, "updateFilterSelected: ${property.selectedText}")
    Log.d(TAG, "updateFilterSelected: ${property.selectedList.size}")
    Log.d(TAG, "updateFilterSelected: ${position}")
    differ.currentList[position].selectedText = property.selectedText
    differ.currentList[position].selectedList = property.selectedList
    notifyItemChanged(position)
  }

  fun replaceSubCategories(it: FilterProperty) {
    differ.currentList.forEachIndexed { index, filterProperty ->
      run {
        if (filterProperty.filterType == FILTER_TYPE.SUB_CATEGORY) {
          filterProperty.children.clear()
          filterProperty.children.addAll(it.children)
          notifyItemChanged(index)
        }
      }
    }
  }

  fun addAll(from: Int,listAddedBefore: Int, filterProperties: ArrayList<FilterProperty>) {
    var array = ArrayList(differ.currentList)
    when(listAddedBefore){
      0 -> baseList.addAll(array)
      else -> {
        array = ArrayList(baseList)
      }
    }
    array.addAll(from,filterProperties)
    differ.submitList(null)
    differ.submitList(array)
  }

  fun update(position: Int, it: FilterProperty) {
    differ.currentList[position].reset()
    differ.currentList[position].children.clear()
    differ.currentList[position].children.addAll(it.children)
    notifyItemChanged(position)
//    var list = ArrayList(differ.currentList)
//    list.set(position,it)
//    notifyItemChanged(position)
  }


  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemFilterBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemFilterViewModel) {
      itemLayoutBinding.itemViewModels = itemViewModel
    }
  }

}