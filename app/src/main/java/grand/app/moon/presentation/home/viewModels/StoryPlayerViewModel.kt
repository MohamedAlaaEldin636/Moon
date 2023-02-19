package grand.app.moon.presentation.home.viewModels

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Application
import androidx.lifecycle.*
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemSegmentBinding
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.constraintPercentWidth
import grand.app.moon.presentation.home.StoryPlayerFragmentArgs
import grand.app.moon.presentation.home.models.ResponseStory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryPlayerViewModel @Inject constructor(
	application: Application,
	val args: StoryPlayerFragmentArgs,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val adapterSegments = AdapterSegments {
		goToNextStory()
	}

	val player by lazy {
		app.createExoPlayer(
			onBufferingVideo = {
				pause()

				viewModelScope.launch {
					awaitReady()

					resume()
				}
			}
		) {
			goToNextStory()
		}
	}

	private val allStoresWithStories = args.jsonOfAllStoresWithStories
		.fromJsonInlinedOrNull<List<ResponseStory>>().orEmpty()

	private val currentIndexOfStoreWithStories = MutableLiveData(args.position)

	val currentStoreWithStories = currentIndexOfStoreWithStories.map {
		allStoresWithStories.getOrNull(it.orZero())
	}

	val currentIndexOfStory = MutableLiveData(0)

	val currentStory = switchMapMultiple2(currentStoreWithStories, currentIndexOfStory) {
		currentStoreWithStories.value?.stories?.getOrNull(currentIndexOfStory.value.orZero())
	}

	val storyLink = currentStory.map {
		it?.linkType
	}

	val phone = currentStoreWithStories.map {
		it?.phone
	}

	val currentDuration = currentStory.asFlow().map {
		if (it?.isVideo.orFalse()) {
			var currentLink = player.currentMediaItem?.localConfiguration?.uri?.toString().orEmpty()
			val toBePlayedLink = it?.file.orEmpty()
			while (currentLink != toBePlayedLink) {
				delay(17)

				currentLink = player.currentMediaItem?.localConfiguration?.uri?.toString().orEmpty()
			}

			while (player.playbackState != Player.STATE_READY) {
				player.awaitReady()
			}

			MyLogger.e("durationnnnnnnn ${player.duration} ==== ${player.totalBufferedDuration}")

			player.duration
		}else {
			10_000L
		}
	}.asLiveData()

	val showImageNotVideo = currentStory.map {
		it?.isVideo.orFalse().not()
	}

	val file = currentStory.map {
		it?.file.orEmpty()
	}

	val finishFragment = MutableLiveData(false)

	fun pause() {
		if (showImageNotVideo.value.orFalse().not()) {
			player.pause()
		}
		adapterSegments.pause()
	}
	fun resume() {
		if (showImageNotVideo.value.orFalse().not()) {
			player.play()
		}
		adapterSegments.resume()
	}

	private fun goToNextStory() {
		if (currentIndexOfStory.value.orZero() < currentStoreWithStories.value?.stories.orEmpty().lastIndex) {
			currentIndexOfStory.value = currentIndexOfStory.value.orZero().inc()
		}else if (currentIndexOfStoreWithStories.value.orZero() < allStoresWithStories.lastIndex) {
			currentIndexOfStoreWithStories.value = currentIndexOfStoreWithStories.value.orZero().inc()
			currentIndexOfStory.value = 0
		}else {
			finishFragment.value = true
		}
	}

	class AdapterSegments(
		private val onAnimationEnd: () -> Unit
	) : RVItemCommonListUsage2<ItemSegmentBinding, Int>(R.layout.item_segment) {

		private var currentSegment: Int = 0

		private var play: Boolean = false
		private var animator: ValueAnimator? = null
		private var currentDurationOfAnimationInMs: Long? = null

		override fun onBind(binding: ItemSegmentBinding, position: Int, item: Int) {
			binding.percentView.constraintPercentWidth(
				if (item < currentSegment) 1f else 0f
			)

			if (item == currentSegment && play) {
				animator?.cancel()
				animator = ValueAnimator.ofFloat(0f, 1f)
				animator?.duration = currentDurationOfAnimationInMs.orZero().coerceAtLeast(300)
				animator?.repeatCount = 0
				animator?.addUpdateListener {
					binding.percentView.constraintPercentWidth(
						(it.animatedValue as? Float).orZero()
					)
				}
				animator?.addListener(object : Animator.AnimatorListener {
					override fun onAnimationStart(animation: Animator) {}
					override fun onAnimationEnd(animation: Animator) {
						play = false
						onAnimationEnd()
					}
					override fun onAnimationCancel(animation: Animator) {}
					override fun onAnimationRepeat(animation: Animator) {}
				})
				animator?.start()
			}
		}

		fun setSegments(count: Int) {
			currentSegment = 0
			submitList(List(count) { it })
		}

		fun setCurrentSegment(index: Int) {
			currentSegment = index
			notifyDataSetChanged()
		}

		fun playCurrentSegment(currentDurationOfAnimationInMs: Long) {
			play = true
			this.currentDurationOfAnimationInMs = currentDurationOfAnimationInMs
			notifyItemChanged(currentSegment)
		}

		fun pause() {
			animator?.pause()
		}

		fun resume() {
			animator?.resume()
		}

	}

}
