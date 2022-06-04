package grand.app.moon.presentation.story.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
import grand.app.moon.presentation.story.storyView.screen.StoryDisplayActivity
import grand.app.moon.presentation.story.viewModels.StoryDisplayViewModel
import grand.app.moon.presentation.subCategory.SubCategoryFragmentArgs
import kotlinx.android.synthetic.main.fragment_ads_list.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import pt.tornelas.segmentedprogressbar.SegmentedProgressBarListener

@AndroidEntryPoint
class StoryFragment : BaseFragment<FragmentStoryBinding>(),
  SegmentedProgressBarListener {
  private val viewModel: StoryDisplayViewModel by viewModels()
  private val args: StoryFragmentArgs by navArgs()

  private val TAG = "StoryFragment"

  override
  fun getLayoutId() = R.layout.fragment_story

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    observe()
    viewModel.stories.addAll(args.stories.list)
    viewModel.positionStoryAdapter = args.stories.position
    viewModel.store.set(viewModel.stories[viewModel.positionStoryAdapter])
    init()

  }


  private fun observe() {
    viewModel.clickEvent.observe(this, {
      when (it) {
        Constants.EXIT -> findNavController().navigateUp()
        Constants.STORE_DETAILS -> {
          findNavController().navigate(
            R.id.nav_store,
            bundleOf(
              "id" to viewModel.store.get()!!.id,
              "type" to 3
            ), Constants.NAVIGATION_OPTIONS
          )
        }
      }
    })
  }

  fun pause() {
    viewModel.progress.set(true)
    binding.progress.pause()
  }

  fun resume() {
    Log.d(TAG, "resume: ${viewModel.isLoaded}")
    if (viewModel.isLoaded) {
      Log.d(TAG, "resume")
      viewModel.progress.set(false)
      binding.progress.start()
    }
  }

  fun init() {
    binding.progress.segmentCount = viewModel.store.get()!!.stories.size
    binding.progress.listener = this
    binding.progress.start()
    binding.skip.setOnClickListener {
      if (viewModel.allowNext())
        binding.progress.next()
      else if (viewModel.nextStory()) {
        pause()
        reset()
      }
    }
    binding.reverse.setOnClickListener {
      if (viewModel.allowPrev())
        binding.progress.previous()
      else if (viewModel.prevStory()) {
        pause()
        reset()
      }
    }

    binding.image.setOnTouchListener(object : View.OnTouchListener {
      override fun onTouch(p0: View?, event: MotionEvent?): Boolean {

        when (event?.action) {
          MotionEvent.ACTION_DOWN -> pause()
          MotionEvent.ACTION_UP -> resume()
        }
        return true
      }

    })

  }

  //
//
  fun loadImage() {
    Log.d(TAG, "loadImage: here")
    Log.d(TAG, "loadImage: ${viewModel.store.get()!!.stories[viewModel.pos].file}")
    viewModel.isLoaded = false
    pause()
    viewModel.image.set(viewModel.store.get()!!.stories[viewModel.pos].file)
    Glide.with(requireContext())
      .load(viewModel.store.get()!!.stories[viewModel.pos].file)
      .listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
          e: GlideException?,
          model: Any?,
          target: Target<Drawable>?,
          isFirstResource: Boolean
        ): Boolean {
          Log.d(TAG, "onLoadFailed: ")
          viewModel.isLoaded = true
          resume()
          return false
        }

        override fun onResourceReady(
          resource: Drawable?,
          model: Any?,
          target: Target<Drawable>?,
          dataSource: com.bumptech.glide.load.DataSource?,
          isFirstResource: Boolean
        ): Boolean {
          Log.d(TAG, "onResourceReady: READY")
          viewModel.isLoaded = true

          resume()
          return false
        }

      })
      .into(binding.image)

  }

  override fun onFinished() {
    viewModel.isFinish = true
    if (viewModel.nextStory()) {
      reset()
    } else
      findNavController().navigateUp()
  }

  override fun onPage(oldPageIndex: Int, newPageIndex: Int) {
    Log.d(TAG, "onPage: $oldPageIndex , withNew $newPageIndex")
    viewModel.pos = newPageIndex
    viewModel.storyRequest.story_id = viewModel.store.get()!!.stories[viewModel.pos].id
    viewModel.isLike.set(viewModel.store.get()!!.stories[viewModel.pos].is_liked)
    viewModel.storyRequest.type = 1
    viewModel.callService()
    loadImage()
  }

  override fun onPause() {
    super.onPause()
    pause()
  }

  override fun onResume() {
    super.onResume()
    if (!viewModel.isFinish)
      resume()
    else {
      reset()
    }
  }

  fun reset() {
    binding.progress.reset()
    viewModel.pos = 0
    viewModel.isFinish = false
    binding.progress.segmentCount = viewModel.store.get()!!.stories.size
    resume()
  }

//  Log.d(TAG, "onProgressStepChange: $newStep")

}