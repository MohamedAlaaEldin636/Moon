package app.grand.tafwak.presentation.reviews.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.grand.tafwak.presentation.reviews.viewModels.ItemReviewViewModel
import grand.app.moon.R
import grand.app.moon.databinding.ItemReviewBinding
import grand.app.moon.domain.home.models.Reviews

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {
  private val differCallback = object : DiffUtil.ItemCallback<Reviews>() {
    override fun areItemsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
      return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
      return oldItem == newItem
    }
  }
    val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemReviewViewModel(data)
    holder.setViewModel(itemViewModel)

  }

  private val TAG = "NotificationAdapter"
  fun insertData(insertList: List<Reviews>) {
    val array = ArrayList<Reviews>(differ.currentList)
    val size = array.size
    array.addAll(insertList)
    Log.d(TAG, "insertData: "+size)
//    notifyItemRangeInserted(size,array.size)
    differ.submitList(array)
    notifyDataSetChanged()
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
    private lateinit var itemLayoutBinding: ItemReviewBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemReviewViewModel) {
      itemLayoutBinding.itemViewModels = itemViewModel
    }
  }

}