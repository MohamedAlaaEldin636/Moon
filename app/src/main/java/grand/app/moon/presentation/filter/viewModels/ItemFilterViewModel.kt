package grand.app.moon.presentation.filter.viewModels

import android.util.Log
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener
import com.rizlee.rangeseekbar.RangeSeekBar
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.filter.FILTER_TYPE


class ItemFilterViewModel  constructor(val model: FilterProperty) : BaseViewModel(), RangeSeekBar.OnRangeSeekBarPostListener {

  var min = ""
  var max = ""
  init {
    min = model.min.toString()
    max = model.max.toString()
  }




  companion object {
    private var event: OnItemClickListener<Any>? = null
    private val TAG: String = "CometChatGroupListScreen"

    /**
     *
     * @param groupItemClickListener An object of `OnItemClickListener<T>` abstract class helps to initialize with events
     * to perform onItemClick & onItemLongClick.
     * @see OnItemClickListener
     */
    @JvmStatic
    fun setItemClickListener(groupItemClickListener: OnItemClickListener<Any>) {
      event = groupItemClickListener
    }
  }

  fun isMinMax(): Boolean{
    return when(model.filterType){
      FILTER_TYPE.PRICE , FILTER_TYPE.PRICE_TEXT-> true
      else -> false
    }
  }

  fun range(): Boolean{
    Log.d(TAG, "range: ${model.type}")
    if(model.filterType == FILTER_TYPE.PRICE || model.type == 3) return true
    return false
  }

  override fun onValuesChanged(minValue: Float, maxValue: Float) {
    Log.d(TAG, "onValuesChanged: THERE $minValue , $maxValue")

  }

  override fun onValuesChanged(minValue: Int, maxValue: Int) {
    Log.d(TAG, "onValuesChanged: THERTE $minValue , $maxValue")
  }
}