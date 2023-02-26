package grand.app.moon.presentation.home

import android.annotation.SuppressLint
import android.media.session.PlaybackState
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.databinding.FragmentStoryPlayerBinding
import grand.app.moon.domain.shop.StoryLink
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.viewModels.StoryPlayerViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class StoryPlayerFragment : BaseFragment<FragmentStoryPlayerBinding>() {

	private val viewModel by viewModels<StoryPlayerViewModel>()

	var job: Job? = null

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
					StoryLink.WHATSAPP -> {
						if (viewModel.getPhoneWithCountryCode().isEmpty()) {
							showMessage(getString(R.string.no_ph_743))
						}else {
							context.launchWhatsApp(viewModel.getPhoneWithCountryCode())
						}
					}
					StoryLink.CALL -> {
						if (viewModel.getPhoneWithCountryCode().isEmpty()) {
							showMessage(getString(R.string.no_ph_743))
						}else {
							context.launchDialNumber(viewModel.getPhoneWithCountryCode())
						}
					}
					StoryLink.CHAT -> viewModel.chat(binding.chatTextView)
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
			val touchX = e.x
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
			}

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

		makeStatusBarTransparentWithWhiteIcons()

		viewModel.resume()
	}

	override fun onPause() {
		viewModel.pause()

		makeStatusBarToPrimaryDarkWithWhiteIcons()

		super.onPause()
	}

	override fun getLayoutId(): Int = R.layout.fragment_story_player

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		MyLogger.e("sahduiashdi onViewCreated")

		super.onViewCreated(view, savedInstanceState)

		viewModel.player = viewModel.app.createExoPlayer(
			viewModel.currentStory.value?.let {
				if (it.isVideo) it.file.orEmpty() else null
			}
		)
		binding.playerView.player = viewModel.player

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapterSegments,
			true,
			1
		) { layoutParams ->
			val number = viewModel.currentStoreWithStories.value?.stories?.size.orZero()
				.coerceAtLeast(1)
			val itemMargins = layoutParams.marginStart + layoutParams.marginEnd

			val totalWidth = width - paddingStart - paddingEnd - (number.dec() * itemMargins)

			layoutParams.width = (totalWidth.toFloat() / number.toFloat()).toInt()
		}

		viewModel.finishFragment.observe(viewLifecycleOwner) {
			MyLogger.e("udhewihdiewh ch 1")

			if (it.orFalse()) {
				viewModel.finishFragment.value = false
			}

			if (it == true) {
				viewModel.pause()
				viewModel.releaseResources()
				viewModel.player = null

				findNavController().navigateUp()
			}
		}

		viewModel.currentStory.observe(viewLifecycleOwner) {
			job?.cancel()

			MyLogger.e("udhewihdiewh ch 2")

			if (it != null && it.isSeen.orFalse().not() && context?.isLogin() == true) {
				context?.applicationScope?.launch {
					viewModel.repoShop.viewStoryInteractions(it.id.orZero())
				}

				it.isSeen = true
			}

			binding.recyclerView.adapter = null
			viewModel.adapterSegments.setSegments(viewModel.currentStoreWithStories.value?.stories?.size.orZero())
			viewModel.adapterSegments.setCurrentSegment(viewModel.currentIndexOfStory.value.orZero())
			binding.recyclerView.adapter = viewModel.adapterSegments

			job = lifecycleScope.launch {
				val duration = if (it?.isVideo.orFalse()) {
					viewModel.player?.changeVideoLink(it?.file.orEmpty(), 0L)

					while (viewModel.player?.playbackState != Player.STATE_READY) {
						viewModel.player?.awaitReady()
					}

					MyLogger.e("durationnnnnnnn ${viewModel.player?.duration} ==== ${viewModel.player?.totalBufferedDuration}")

					viewModel.player?.duration ?: 10_000L
				}else {
					10_000L
				}

				if (viewModel.currentStory.value?.isVideo.orFalse()) {
					MyLogger.e("udhewihdiewh ch 3.1")
					viewModel.player?.awaitPlaying()
					MyLogger.e("udhewihdiewh ch 3.3")
				}else {
					viewModel.player?.pause()
				}
				MyLogger.e("udhewihdiewh ch 3.4")
				viewModel.adapterSegments.playCurrentSegment(duration, viewModel.currentIndexOfStory.value.orZero())
				MyLogger.e("udhewihdiewh ch 3.5")
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

	override fun onDestroyView() {
		MyLogger.e("sahduiashdi onDestroyView")

		viewModel.finishFragment.removeObservers(viewLifecycleOwner)
		viewModel.currentStory.removeObservers(viewLifecycleOwner)

		viewModel.pause()
		viewModel.releaseResources()
		viewModel.player = null

		super.onDestroyView()
	}

}
