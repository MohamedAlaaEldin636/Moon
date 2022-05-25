package grand.app.moon.presentation.story.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemStoryAllBinding
import grand.app.moon.databinding.ItemStoryBinding
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.home.models.StoreModel
import grand.app.moon.domain.store.entity.StoreListPaginateData
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.base.extensions.navigateSafe
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.home.HomeFragment
import grand.app.moon.presentation.home.HomeFragmentDirections
import grand.app.moon.presentation.story.storyView.screen.StoryDisplayActivity
import grand.app.moon.presentation.story.view.StoriesListFragment
import grand.app.moon.presentation.story.view.StoriesListFragmentDirections
import grand.app.moon.presentation.story.viewModels.ItemStoryViewModel
import java.io.Serializable

class StoriesAllAdapter : RecyclerView.Adapter<StoriesAllAdapter.ViewHolder>() {
  lateinit var context: Context
  var clickEvent: SingleLiveEvent<Store> = SingleLiveEvent()
  val storiesPaginate = StoreListPaginateData()

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
      .inflate(R.layout.item_story_all, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  private val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemStoryViewModel(data)
    holder.itemLayoutBinding.itemMore.setOnClickListener {
//      Log.d(TAG, "onBindViewHolder: ")
//      clickEvent.value = data

//      val intent =
//        Intent(holder.itemLayoutBinding.root.context, StoryDisplayActivity::class.java)
//      val bundle = Bundle()
//      bundle.putSerializable(Constants.STORIES, differ.currentList as Serializable)
//      bundle.putInt(Constants.POSITION_SELECT,position)
//      intent.putExtra(Constants.BUNDLE,bundle)
//      holder.itemLayoutBinding.root.context.startActivity(intent)
      val storyModel = StoreModel()
      storyModel.list.addAll(differ.currentList)
      storyModel.position = position
      holder.itemLayoutBinding.itemMore.findFragment<StoriesListFragment>()
        .navigateSafe(StoriesListFragmentDirections.actionStoriesListFragmentToStoryFragment(storyModel))


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
    lateinit var itemLayoutBinding: ItemStoryAllBinding

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