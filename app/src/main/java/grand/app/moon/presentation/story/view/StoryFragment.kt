package grand.app.moon.presentation.story.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bolaware.viewstimerstory.Momentz
import com.bolaware.viewstimerstory.MomentzCallback
import com.bolaware.viewstimerstory.MomentzView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.databinding.FragmentNotificationBinding
import grand.app.moon.databinding.FragmentStoryBinding
import grand.app.moon.domain.home.models.Store
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SwipeToDeleteCallback
import grand.app.moon.presentation.home.viewModels.HomeViewModel
import grand.app.moon.presentation.notfication.viewmodel.NotificationListViewModel
import grand.app.moon.presentation.story.viewModels.StoryDisplayViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StoryFragment : BaseFragment<FragmentStoryBinding>(),
  MomentzCallback {
  lateinit var momentz: Momentz
  private val viewModel: StoryDisplayViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_story

  private  val TAG = "StoryFragment"
  override
  fun setBindingVariables() {
    binding.viewModel = viewModel

//    if(viewModel.stories.isNotEmpty()) {
    var dataList = ArrayList<Store>(arguments?.get(Constants.STORIES) as ArrayList<Store>)

    viewModel.pos = arguments?.getInt(Constants.POSITION)!!
    viewModel.store.set(dataList[viewModel.pos])
    prepareStories()
//    }
//    startStory()
  }


  private fun prepareStories() {
    viewModel.store.get()!!.stories.forEach { story ->
      val locallyLoadedImageView = AppCompatImageView(requireContext())
      locallyLoadedImageView.scaleType = ImageView.ScaleType.FIT_CENTER
      viewModel.listStories.add(MomentzView(locallyLoadedImageView, 5))
    }
  }

  fun startStory() {
    if (this::momentz.isInitialized) {
      momentz.removeAllViews()
      binding.container.removeAllViews()
    }
    momentz = Momentz(requireContext(), viewModel.listStories, binding.container, this)
    momentz.start()
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  fun loadImage(view: AppCompatImageView?, momentz: Momentz?, index: Int) {
    if (index >= 0 && index < viewModel.store.get()!!.stories.size) {
      momentz?.pause(false)
      viewModel.startLoading()
      viewModel.image.set(viewModel.store.get()!!.stories[index].file)
      view?.let {
        Glide.with(this)
          .load(viewModel.store.get()!!.stories[index].file)
          .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
              e: GlideException?,
              model: Any?,
              target: Target<Drawable>?,
              isFirstResource: Boolean
            ): Boolean {
              try {
                momentz?.resume()
              }catch (e: Exception){
                e.printStackTrace()
              }
              viewModel.stopLoading()
              return false
            }

            override fun onResourceReady(
              resource: Drawable?,
              model: Any?,
              target: Target<Drawable>?,
              dataSource: com.bumptech.glide.load.DataSource?,
              isFirstResource: Boolean
            ): Boolean {
              viewModel.stopLoading()
              try {
                momentz?.resume()
              }catch (e: Exception){
                e.printStackTrace()
              }
              return false
            }

          })
          .into(it)
      }

    } else {
//      finish()
    }
  }


  override
  fun setUpViews() {
  }


  override fun setupObservers() {
    viewModel.clickEvent.observe(this, {
      when (it) {
        Constants.SHARE_IMAGE -> {
          viewModel.storyRequest.type = 3
          viewModel.callService()
          viewModel.share(requireContext(), viewModel.store.get()!!.name, "", binding.imgShare)
        }
//        Constants.EXIT -> finish()
        Constants.STORE_DETAILS -> {

        }
      }
    })
  }


  override fun done() {
//    when(viewModel.isFinish()){
//      true -> finish()
//      else -> {
//        prepareStories()
//        startStory()
//      }
//    }
  }


  override fun onNextCalled(view: View, momentz: Momentz, index: Int) {
    viewModel.index = index
    viewModel.storyRequest.story_id = viewModel.store.get()!!.stories[index].id
    viewModel.storyRequest.type = 1
    viewModel.callService()
    loadImage(view as AppCompatImageView, momentz, index)
  }

  override fun onPause() {
    super.onPause()
  }

  override fun onResume() {
    super.onResume()
    startStory()
  }
}