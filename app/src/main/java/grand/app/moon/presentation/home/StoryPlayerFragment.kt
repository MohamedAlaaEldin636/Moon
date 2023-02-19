package grand.app.moon.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.databinding.FragmentStoryPlayerBinding
import grand.app.moon.domain.shop.StoryLink
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.home.viewModels.StoryPlayerViewModel
import kotlinx.coroutines.launch

// todo icon white opacity 5% isa.
// todo on end of story leh bta5od wa2t 3ala ma tgeb el store elle ba3do kda isa ?!
@AndroidEntryPoint
class StoryPlayerFragment : BaseFragment<FragmentStoryPlayerBinding>() {

	private val viewModel by viewModels<StoryPlayerViewModel>()

	private val listener: GestureDetector.SimpleOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
		override fun onFling(
			e1: MotionEvent,
			e2: MotionEvent,
			velocityX: Float,
			velocityY: Float
		): Boolean {
			val context = _binding?.root?.context

			if (velocityY < 0 && context != null) {
				// Swipe Up
				when (viewModel.storyLink.value) {
					StoryLink.WHATSAPP -> context.launchWhatsApp(viewModel.phone.value.orEmpty())
					StoryLink.CALL -> context.launchDialNumber(viewModel.phone.value.orEmpty())
					StoryLink.CHAT -> {
						if (context.isLoginWithOpenAuth()) {
							viewModel.currentStoreWithStories.value?.also {
								context.openChatStore(binding.root, it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
							}
						}
					}
					null -> {}
				}
			}

			return super.onFling(e1, e2, velocityX, velocityY)
		}

		override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
			MyLogger.e("aaaidosjdoiasjdaaaa onSingleTapConfirmed")
			return super.onSingleTapConfirmed(e)
		}

		override fun onSingleTapUp(e: MotionEvent): Boolean {
			MyLogger.e("aaaidosjdoiasjdaaaa onSingleTapUp")
			/*val touchX = e.x
			val viewWidth = _binding?.constraintLayout?.width.orZero()
			if (viewWidth > 0) {
				val isRtl = context?.resources?.getBoolean(R.bool.is_rtl).orFalse()
				val goToNextStory = if (touchX < viewWidth / 2) {
					// Touch is on the left side of the view
					 isRtl
				}else {
					// Touch is on the right side of the view
					isRtl.not()
				}

				if (goToNextStory) {
					viewModel.goToNextStory()
				}else {
					viewModel.goToPrevStory()
				}
			}*/

			return super.onSingleTapUp(e)
		}

		override fun onDown(e: MotionEvent): Boolean {
			MyLogger.e("aaaidosjdoiasjdaaaa onDown")
			return super.onDown(e)
		}
	}

	private val gestureDetector by lazy {
		GestureDetector(context ?: return@lazy null, listener)
	}

	override fun onResume() {
		super.onResume()

		viewModel.resume()

		makeStatusBarTransparentWithWhiteIcons()
	}

	override fun onPause() {
		makeStatusBarToPrimaryDarkWithWhiteIcons()

		viewModel.pause()

		super.onPause()
	}

	override fun getLayoutId(): Int = R.layout.fragment_story_player

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapterSegments,
			true,
			1
		) { layoutParams ->
			layoutParams.width = width / viewModel.currentStoreWithStories.value?.stories?.size.orZero()
				.coerceAtLeast(1)
		}

		viewModel.finishFragment.observe(viewLifecycleOwner) {
			MyLogger.e("udhewihdiewh ch 1")

			if (it.orFalse()) {
				viewModel.finishFragment.value = false
			}

			if (it == true) {
				findNavController().navigateUp()
			}
		}

		viewModel.currentStory.observe(viewLifecycleOwner) {
			MyLogger.e("udhewihdiewh ch 2")

			if (it != null && it.isSeen.orFalse().not() && context?.isLogin() == true) {
				context?.applicationScope?.launch {
					viewModel.repoShop.viewStoryInteractions(it.id.orZero())
				}

				it.isSeen = true
			}

			binding.recyclerView.post {
				viewModel.adapterSegments.setSegments(viewModel.currentStoreWithStories.value?.stories?.size.orZero())
				viewModel.adapterSegments.setCurrentSegment(viewModel.currentIndexOfStory.value.orZero())
			}
		}

		viewModel.currentDuration.observe(viewLifecycleOwner) {
			MyLogger.e("udhewihdiewh ch 3")

			binding.recyclerView.post {
				viewModel.adapterSegments.playCurrentSegment(it.orZero())
			}
		}

		@Suppress("ObjectLiteralToLambda")
		binding.constraintLayout.setOnTouchListener(object : View.OnTouchListener {
			@SuppressLint("ClickableViewAccessibility")
			override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
				when (event?.action) {
					MotionEvent.ACTION_DOWN -> {
						viewModel.pause()
					}
					MotionEvent.ACTION_UP -> {
						viewModel.resume()
					}
				}
				if (event != null) {
					gestureDetector?.onTouchEvent(event)
				}

				return true
			}
		})
	}

}
