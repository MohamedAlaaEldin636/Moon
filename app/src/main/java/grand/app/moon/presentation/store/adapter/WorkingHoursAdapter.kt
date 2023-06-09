package grand.app.moon.presentation.store.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemWorkingHoursBinding
import grand.app.moon.domain.home.models.store.WorkingHours
import grand.app.moon.presentation.ads.viewModels.ItemPropertyViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.store.viewModels.ItemWorkingHoursViewModel

class WorkingHoursAdapter : RecyclerView.Adapter<WorkingHoursAdapter.ViewHolder>() {
  lateinit var context: Context
  var clickEvent: SingleLiveEvent<WorkingHours> = SingleLiveEvent()

  private val differCallback = object : DiffUtil.ItemCallback<WorkingHours>() {
    override fun areItemsTheSame(
      oldItem: WorkingHours,
      newItem: WorkingHours
    ): Boolean {
      return oldItem == newItem
    }

    override fun areContentsTheSame(
      oldItem: WorkingHours,
      newItem: WorkingHours
    ): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_working_hours, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  private  val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemWorkingHoursViewModel(data)
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
    lateinit var itemLayoutBinding: ItemWorkingHoursBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemWorkingHoursViewModel) {
      itemLayoutBinding.viewModel = itemViewModel
    }
  }

}