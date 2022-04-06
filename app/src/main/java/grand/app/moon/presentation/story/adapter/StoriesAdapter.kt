package grand.app.moon.presentation.story.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemStoryBinding
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.story.storyView.screen.StoryDisplayActivity
import grand.app.moon.presentation.story.viewModels.ItemStoryViewModel

class StoriesAdapter : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {
  lateinit var context: Context
  var clickEvent: SingleLiveEvent<Store> = SingleLiveEvent()

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
      .inflate(R.layout.item_story, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  private  val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemStoryViewModel(data)
    holder.itemLayoutBinding.itemMore.setOnClickListener {
//      Log.d(TAG, "onBindViewHolder: ")
//      clickEvent.value = data
      val intent = Intent(holder.itemLayoutBinding.root.context,StoryDisplayActivity::class.java)
      intent.putExtra(Constants.STORIES,data)
      holder.itemLayoutBinding.root.context.startActivity(intent)
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
    lateinit var itemLayoutBinding: ItemStoryBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemStoryViewModel) {
      itemLayoutBinding.itemViewModels = itemViewModel
    }
  }

}