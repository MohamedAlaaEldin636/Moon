package app.grand.tafwak.presentation.social.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.grand.tafwak.domain.settings.models.SettingsData
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.utils.openBrowser
import com.structure.base_mvvm.databinding.ItemSocialBinding
import app.grand.tafwak.presentation.social.viewModels.ItemSocialViewModel

class SocialAdapter : RecyclerView.Adapter<SocialAdapter.ViewHolder>() {
  lateinit var context: Context
  private val differCallback = object : DiffUtil.ItemCallback<SettingsData>() {
    override fun areItemsTheSame(oldItem: SettingsData, newItem: SettingsData): Boolean {
      return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: SettingsData, newItem: SettingsData): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_social, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemSocialViewModel(data)
    itemViewModel.clickEvent.observeForever {
      openBrowser(context, data.content)
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
    private lateinit var itemLayoutBinding: ItemSocialBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemSocialViewModel) {
      itemLayoutBinding.notifyItemViewModels = itemViewModel
    }
  }

}