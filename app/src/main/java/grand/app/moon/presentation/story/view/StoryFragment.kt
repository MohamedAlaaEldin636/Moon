package grand.app.moon.presentation.story.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
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
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import grand.app.moon.domain.utils.Resource
import grand.app.moon.R
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.appMoonHelper.FilterDialog
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.databinding.FragmentNotificationBinding
import grand.app.moon.databinding.FragmentStoryBinding
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.shop.StoryLink
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.extensions.*
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
import pt.tornelas.segmentedprogressbar.SegmentedProgressBar
import pt.tornelas.segmentedprogressbar.SegmentedProgressBarListener
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

@AndroidEntryPoint
class StoryFragment : BaseFragment<FragmentStoryBinding>(),
  SegmentedProgressBarListener {
  private val viewModel: StoryDisplayViewModel by viewModels()

  private val TAG = "StoryFragment"

	private val listener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
		override fun onFling(
			e1: MotionEvent,
			e2: MotionEvent,
			velocityX: Float,
			velocityY: Float
		): Boolean {
			if (velocityY < 0) {
				// Swipe Up
				when (viewModel.storyLink.value) {
					StoryLink.WHATSAPP -> context?.launchWhatsApp(viewModel.phone.value.orEmpty())
					StoryLink.CALL -> context?.launchDialNumber(viewModel.phone.value.orEmpty())
					StoryLink.CHAT -> viewModel.chat(binding.edtLoginPhone)
					null -> {}
				}
			}

			return super.onFling(e1, e2, velocityX, velocityY)
		}
	}

	private val gestureDetector by lazy {
		GestureDetector(context ?: return@lazy null, listener)
	}

  override
  fun getLayoutId() = R.layout.fragment_story

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    observe()
    if (arguments?.containsKey("store") == true && arguments?.getString("store")
        ?.isNotEmpty() == true
    ) {
      val gson = GsonBuilder().create()
      val store = gson.fromJson<Store>(requireArguments().getString("store"), object :
        TypeToken<Store>() {}.type)
      viewModel.stories.add(store)
      viewModel.positionStoryAdapter = 0
      viewModel.fromStore = true
      Log.d(TAG, "setBindingVariables: YES NOT NULL")
    } else {
      val args: StoryFragmentArgs by navArgs()
      Log.d(TAG, "setBindingVariables: YES  NULL")
      viewModel.stories.addAll(args.stories.list)
      viewModel.positionStoryAdapter = args.stories.position
    }
    viewModel.store.set(viewModel.stories[viewModel.positionStoryAdapter])
    init()

  }


  private fun observe() {
    viewModel.clickEvent.observe(this) {
	    when (it) {
		    Constants.EXIT -> findNavController().navigateUp()
		    Constants.STORE_DETAILS -> {
			    when (viewModel.fromStore) {
				    true -> findNavController().navigateUp()
				    else -> findNavController().navigate(
					    R.id.nav_store,
					    bundleOf(
						    "id" to viewModel.store.get()!!.id,
						    "type" to 3
					    ), Constants.NAVIGATION_OPTIONS
				    )
			    }
		    }
	    }
    }
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
    binding.progress.segmentCount = viewModel.store.get()?.stories?.size.orZero()
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
	      if (event != null) {
		      gestureDetector?.onTouchEvent(event)
	      }

        return true
      }
    })
  }

  fun loadImage() {
    Log.d(TAG, "loadImage: here")
    Log.d(TAG, "loadImage: ${viewModel.store.get()?.stories?.getOrNull(viewModel.pos)?.file.orEmpty()}")
    viewModel.isLoaded = false
    pause()
    viewModel.image.set(viewModel.store.get()?.stories?.getOrNull(viewModel.pos)?.file.orEmpty())
    try {
      Glide.with(requireContext())
        .load(viewModel.store.get()?.stories?.getOrNull(viewModel.pos)?.file.orEmpty())
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
    }catch (e:Exception){
      e.printStackTrace()
    }

  }

  override fun onFinished() {
    viewModel.isFinish = true
    if (viewModel.nextStory()) {
      reset()
    } else
      findNavController().navigateUp()
  }

  override fun onPage(oldPageIndex: Int, newPageIndex: Int) {
	  //MyLogger.e("sijdaoisdjoa $oldPageIndex $newPageIndex") -1, 0, 1, etc...
	  /*if (newPageIndex == 0) {
		  binding.progress.setDeclaredMemberProperty("timePerSegmentMs", 40_000L)
	  }else {
		  binding.progress.setDeclaredMemberProperty("timePerSegmentMs", 3_000L)
	  }*/
    Log.d(TAG, "onPage: $oldPageIndex , withNew $newPageIndex")
    viewModel.pos = newPageIndex
    viewModel.storyRequest.story_id = viewModel.store.get()?.stories?.getOrNull(viewModel.pos)?.id.orZero()
    viewModel.isLike.set(viewModel.store.get()?.stories?.getOrNull(viewModel.pos)?.is_liked.orFalse())
    viewModel.storyRequest.type = 1
	  viewModel.storyLink.value = StoryLink.values().firstOrNull {
			it.apiValue == viewModel.store.get()?.stories?.getOrNull(viewModel.pos)?.storyLinkType
	  }
	  viewModel.phone.value = viewModel.store.get()?.phone
    if (binding.root.context.isLogin()) {
      viewModel.callService()
    }
    loadImage()
  }

  override fun onPause() {
    super.onPause()
    pause()
  }

  override fun onResume() {
    super.onResume()
    if (!viewModel.isFinish) {
      if (viewModel.checkBlockStore())
        backToPreviousScreen()
      else resume()
    } else {
      reset()
    }
  }

  fun reset() {
    binding.progress.reset()
    viewModel.pos = 0
    viewModel.isFinish = false
    binding.progress.segmentCount = viewModel.store.get()?.stories?.size.orZero()
    resume()
  }

//  Log.d(TAG, "onProgressStepChange: $newStep")

}