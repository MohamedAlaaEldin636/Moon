package grand.app.moon.presentation.store.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemStoreBinding
import grand.app.moon.domain.home.models.Store
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.viewModels.ItemStoreViewModel

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
  lateinit var context: Context
  var submitEvent: MutableLiveData<String> = MutableLiveData()
  var position = 0
  private val differCallback = object : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(
      oldItem: Store,
      newItem: Store
    ): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
      oldItem: Store,
      newItem: Store
    ): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_store, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  private  val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemStoreViewModel(data)
    holder.itemLayoutBinding.itemStoreContainer.setOnClickListener {
      Log.d(TAG, "onBindViewHolder: HAY HERE")

      val options = NavOptions.Builder()
        .setEnterAnim(R.anim.anim_slide_in_right)
        .setExitAnim(R.anim.anim_slide_out_left)
        .setPopEnterAnim(R.anim.anim_slide_in_left)
        .setPopExitAnim(R.anim.anim_slide_out_right)
        .build()
      holder.itemLayoutBinding.root.findNavController().navigate(R.id.nav_store,
        bundleOf(
          "id" to data.id
        ),options)


//      this.position = position
//      submitEvent.value = Constants.SUBMIT
    }
    holder.itemLayoutBinding.follow.setOnClickListener {
      Log.d(TAG, "onBindViewHolder: HAY THERE")
      data.isFollowing = data.isFollowing != true
      this.position = position
      submitEvent.value = Constants.FOLLOW
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
    lateinit var itemLayoutBinding: ItemStoreBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemStoreViewModel) {
      itemLayoutBinding.itemViewModels = itemViewModel
    }
  }

}