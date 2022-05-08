package grand.app.moon.presentation.filter.dialog.multiCheck

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.countries.entity.Country
//import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.R
import grand.app.moon.presentation.auth.countries.viewModels.ItemCountryViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.databinding.ItemCountryBinding
import grand.app.moon.databinding.ItemFilterMultiSelectBinding
import grand.app.moon.databinding.ItemReportBinding
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.presentation.store.viewModels.ItemReportViewModel

class FilterMultiAdapter : RecyclerView.Adapter<FilterMultiAdapter.ViewHolder>() {
  val selected = ArrayList<Int>()
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
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter_multi_select, parent, false)
    return ViewHolder(view)
  }

  private  val TAG = "CountriesAdapter"
  @SuppressLint("RecyclerView")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemFilterMultiViewModel(data,position,selected.contains(data.id))
    itemViewModel.clickEvent.observeForever {
      val children: AppTutorial = differ.currentList[position]
      if (selected.contains(children.id)) {
        selected.removeAt(selected.indexOf(children.id))
      } else {
        selected.add(children.id)
      }
      notifyItemChanged(position)
    }
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
    lateinit var itemLayoutBinding: ItemFilterMultiSelectBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemFilterMultiViewModel) {
      itemLayoutBinding.viewModel = itemViewModel
    }
  }
}