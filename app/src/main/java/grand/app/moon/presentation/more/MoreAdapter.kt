package grand.app.moon.presentation.more

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemMoreBinding
import grand.app.moon.presentation.base.utils.SingleLiveEvent

class MoreAdapter : RecyclerView.Adapter<MoreAdapter.ViewHolder>() {
  lateinit var context: Context
  var clickEvent: MutableLiveData<MoreItem> = MutableLiveData()

  private val differCallback = object : DiffUtil.ItemCallback<MoreItem>() {
    override fun areItemsTheSame(
      oldItem: MoreItem,
      newItem: MoreItem
    ): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
      oldItem: MoreItem,
      newItem: MoreItem
    ): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_more, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  private  val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemMoreViewModel(data)
    holder.itemLayoutBinding.itemMore.setOnClickListener {
      clickEvent.value = data
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
    lateinit var itemLayoutBinding: ItemMoreBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemMoreViewModel) {
      itemLayoutBinding.itemViewModels = itemViewModel
    }
  }

}