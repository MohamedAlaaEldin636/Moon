package grand.app.moon.presentation.filter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
//import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.R
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.databinding.ItemReportBinding
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.presentation.store.viewModels.ItemReportViewModel

class FilterRateAdapter : RecyclerView.Adapter<FilterRateAdapter.ViewHolder>() {
  var lastSelected = -1
  var lastPosition = -1
  var changeEvent: SingleLiveEvent<AppTutorial> = SingleLiveEvent()
  private val differCallback = object : DiffUtil.ItemCallback<AppTutorial>() {
    override fun areItemsTheSame(oldItem: AppTutorial, newItem: AppTutorial): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AppTutorial, newItem: AppTutorial): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter_rate, parent, false)
    return ViewHolder(view)
  }

  private  val TAG = "CountriesAdapter"
  @SuppressLint("RecyclerView")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemReportViewModel(data,position == lastPosition)
    itemViewModel.clickEvent.observeForever {
//      Log.d(TAG, "onBindViewHolder: $lastPosition")
      if(lastPosition != -1) {
        differ.currentList[lastPosition].active = 0
        notifyItemChanged(lastPosition)
      }
      lastPosition = position
      lastSelected = data.id
      differ.currentList[position].active = 1
      changeEvent.value = data // for listen on view
      notifyItemChanged(position)
    }
    holder.itemLayoutBinding.radio.isChecked = lastPosition == position
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

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemReportBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemReportViewModel) {
      itemLayoutBinding.countryItemViewModels = itemViewModel
    }
  }
}