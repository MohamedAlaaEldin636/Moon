package app.grand.tafwak.presentation.reviews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.grand.tafwak.domain.reviews.entity.Reviews
import com.structure.base_mvvm.R
import com.structure.base_mvvm.databinding.ItemReviewBinding
import app.grand.tafwak.presentation.reviews.viewModels.ItemReviewViewModel

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {
  private val differCallback = object : DiffUtil.ItemCallback<Reviews>() {
    override fun areItemsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
      return oldItem.user == newItem.user
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