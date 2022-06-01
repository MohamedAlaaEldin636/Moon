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
import grand.app.moon.appMoonHelper.ListHelper
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
import grand.app.moon.presentation.story.viewModels.ItemStoryViewModel
import java.io.Serializable

class StoriesAdapter : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {
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
      .inflate(R.layout.item_story, parent, false)
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
      when (position) {
        0 -> {
          holder.itemLayoutBinding.root.findNavController().navigate(
            R.id.to_stories,
            bundleOf(
              "stories" to storiesPaginate
            ), Constants.NAVIGATION_OPTIONS
          )
        }
        else -> {
          val list = ArrayList(differ.currentList)
          list.removeAt(0)
          val storyModel = StoreModel()
          storyModel.list.addAll(list)
          storyModel.position = position - 1
          holder.itemLayoutBinding.shapeableImageView.findFragment<HomeFragment>()
            .navigateSafe(HomeFragmentDirections.actionHomeFragmentToStoryFragment(storyModel))
        }
      }
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

  fun checkBlockStore() {
    val array = ArrayList(differ.currentList)
    differ.currentList.forEachIndexed { index, store ->
      if (ListHelper.checkBlockStore(differ.currentList[index].id)) {
        array.removeAt(index)
//          notifyItemRemoved(index)
      }
    }
    if (array.size != differ.currentList.size) {
      differ.submitList(null)
      differ.submitList(array)
    }
  }

  var checked = false
  fun viewedStores() {
    val list = ArrayList(differ.currentList)
    list.forEachIndexed { index, it ->
      checked = false
      if (ListHelper.isViewedStory(it.id)) {
        it.stories.forEach {
          it.isSeen = true
        }
        checked = true
      }
      it.stories.forEachIndexed { index , story ->
        if(story.is_liked !=  ListHelper.isStoryLike(story.id)) {
          story.is_liked = ListHelper.isStoryLike(story.id)
          checked = true
        }
      }
      if (checked) notifyItemChanged(index)
    }
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