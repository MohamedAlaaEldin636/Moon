package grand.app.moon.presentation.home.viewModels

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Application
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.core.extenstions.launchCometChat
import grand.app.moon.core.extenstions.openChatStore
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemSegmentBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.shop.StoryLink
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.constraintPercentWidth
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.StoryPlayerFragment
import grand.app.moon.presentation.home.StoryPlayerFragmentArgs
import grand.app.moon.presentation.home.models.ResponseStory
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryPlayerViewModel @Inject constructor(
	application: Application,
	val args: StoryPlayerFragmentArgs,
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase,
) : AndroidViewModel(application) {

	val adapterSegments = AdapterSegments {
		goToNextStory()
	}

	var player: ExoPlayer? = null

	private val allStoresWithStories = args.jsonOfAllStoresWithStories
		.fromJsonInlinedOrNull<List<ResponseStory>>().orEmpty().also {
			MyLogger.e("hhhhhhhhhhhhhhhhhhhhhhhhh 1 -> $it")
		}

	private val currentIndexOfStoreWithStories = MutableLiveData(args.position)

	val currentStoreWithStories = currentIndexOfStoreWithStories.map {
		it.also {
			MyLogger.e("hhhhhhhhhhhhhhhhhhhhhhhhh 2 -> $it")
		}
		allStoresWithStories.getOrNull(it.orZero())
	}

	val isSouqMoonStory = currentStoreWithStories.map {
		it?.isSouqMoonStory.orFalse()
	}

	val currentIndexOfStory = MutableLiveData(0)

	val currentStory = switchMapMultiple2(currentStoreWithStories, currentIndexOfStory) {
		currentStoreWithStories.value?.stories?.getOrNull(currentIndexOfStory.value.orZero())
	}

	val storyLink = currentStory.map {
		it?.linkType
	}

	val drawableOfStoryLink = storyLink.map {
		val drawableRes = when (it) {
			StoryLink.WHATSAPP -> R.drawable.story_whatsapp
			StoryLink.CALL -> R.drawable.story_call
			StoryLink.CHAT -> R.drawable.story_chat
			null -> return@map null
		}

		ContextCompat.getDrawable(application, drawableRes)
	}

	val textOfStoryLink = storyLink.map {
		val textRes = when (it) {
			StoryLink.WHATSAPP -> R.string.whatsapp_3
			StoryLink.CALL -> R.string.call
			StoryLink.CHAT -> R.string.chat_3829
			null -> return@map null
		}

		application.getString(textRes)
	}

	val drawableOfLikeIcon = currentStory.mapToMutableLiveData {
		val drawableRes = if (it?.isLiked.orFalse()) R.drawable.ic_like_fill else R.drawable.like

		ContextCompat.getDrawable(application, drawableRes)
	}

	fun getPhoneWithCountryCode() = currentStoreWithStories.value?.let {
		"${it.countryCode.orEmpty()}${it.phone.orEmpty()}"
	}.orEmpty()

	/*val phone = currentStoreWithStories.map {
		it.also {
			MyLogger.e("hhhhhhhhhhhhhhhhhhhhhhhhh 3 -> ${it?.phone} -> $it")
		}

		it?.phone
	}*/

	val showImageNotVideo = currentStory.map {
		it?.isVideo.orFalse().not()
	}

	val file = currentStory.map {
		it?.file.orEmpty()
	}

	val finishFragment = MutableLiveData(false)

	val storeLogoImage = currentStoreWithStories.map {
		it?.image.orEmpty()
	}

	val storeName = currentStoreWithStories.map {
		it?.name.orEmpty()
	}

	val storeDate = currentStoreWithStories.map {
		it?.createdAt.orEmpty()
	}

	val storeNickName = currentStoreWithStories.map {
		"( ${it?.nickname.orEmpty()} )"
	}

	fun actOnStoryLink(view: View) {
		val fragment = view.findFragmentOrNull<StoryPlayerFragment>() ?: return

		val context = view.context ?: return

		when (storyLink.value) {
			StoryLink.WHATSAPP -> {
				if (getPhoneWithCountryCode().isEmpty()) {
					fragment.showMessage(getString(R.string.no_ph_743))
				}else {
					MyLogger.e("dejwkejhdkwejd ${getPhoneWithCountryCode()}")

					context.applicationScope?.launch {
						repoShop.interactionForStoreWhatsApp(currentStoreWithStories.value?.id.orZero())
					}

					context.launchWhatsApp(getPhoneWithCountryCode())
				}
			}
			StoryLink.CALL -> {
				if (getPhoneWithCountryCode().isEmpty()) {
					fragment.showMessage(getString(R.string.no_ph_743))
				}else {
					MyLogger.e("dejwkejhdkwejd ${getPhoneWithCountryCode()}")

					context.applicationScope?.launch {
						repoShop.interactionForStoreCall(currentStoreWithStories.value?.id.orZero())
					}

					context.launchDialNumber(getPhoneWithCountryCode())
				}
			}
			StoryLink.CHAT -> chat(fragment.binding.chatTextView)
			null -> {}
		}
	}

	fun pause() {
		if (showImageNotVideo.value.orFalse().not()) {
			player?.pause()
		}
		adapterSegments.pause()
	}
	fun resume() {
		if (showImageNotVideo.value.orFalse().not()) {
			player?.play()
		}
		adapterSegments.resume()
	}

	fun goToNextStory() {
	  pause()

		MyLogger.e("currentIndexOfStory.value.orZero() < currentStoreWithStories.value?.stories.orEmpty().lastIndex " +
			"${currentIndexOfStory.value.orZero()} ${currentStoreWithStories.value?.stories.orEmpty().lastIndex}")
		if (currentIndexOfStory.value.orZero() < currentStoreWithStories.value?.stories.orEmpty().lastIndex) {
			currentIndexOfStory.value = currentIndexOfStory.value.orZero().inc()
		}else if (currentIndexOfStoreWithStories.value.orZero() < allStoresWithStories.lastIndex) {
			currentIndexOfStoreWithStories.value = currentIndexOfStoreWithStories.value.orZero().inc()
			currentIndexOfStory.value = 0
		}else {
			finishFragment.value = true
		}
	}

	fun goToPrevStory() {
		pause()

		if (currentIndexOfStory.value.orZero() > 0) {
			currentIndexOfStory.value = currentIndexOfStory.value.orZero().dec()
		}else if (currentIndexOfStoreWithStories.value.orZero() > 0) {
			currentIndexOfStoreWithStories.value = currentIndexOfStoreWithStories.value.orZero().dec()
			currentIndexOfStory.value = 0
		}else {
			finishFragment.value = true
		}
	}

	fun goToPrevScreen() {
		finishFragment.value = true
	}

	fun releaseResources() {
		pause()

		player?.release()
	}

	fun share(view: View) {
		val context = view.context ?: return

		context.launchShareText(
			currentStoreWithStories.value?.name.orEmpty(),
			currentStory.value?.shareLink.orEmpty()
		)

		context.applicationScope?.launch {
			repoShop.shareStoryInteractions(currentStory.value?.id ?: return@launch)
		}
	}

	fun like(view: View) {
		val context = view.context ?: return

		val fragment = view.findFragmentOrNull<StoryPlayerFragment>() ?: return

		if (context.isLoginWithOpenAuth()) {
			if (currentStoreWithStories.value?.id.orZero() == userLocalUseCase().id) {
				// My Store
				return fragment.showMessage(context.getString(R.string.you_can_t_like_your_own_story))
			}

			val currentStoreId = currentStoreWithStories.value?.id.orZero()
			val currentStoryId = currentStory.value?.id.orZero()
			val newIsLiked = currentStory.value?.isLiked.orFalse().not()

			allStoresWithStories.firstOrNull { it.id == currentStoreId }?.stories
				?.firstOrNull { it.id == currentStoryId }?.isLiked = newIsLiked
			currentStoreWithStories.value?.stories
				?.firstOrNull { it.id == currentStoryId }?.isLiked = newIsLiked
			currentStory.value?.isLiked = newIsLiked
			drawableOfLikeIcon.value = (if (newIsLiked) R.drawable.ic_like_fill else R.drawable.like).let {
				ContextCompat.getDrawable(app, it)
			}

			context.applicationScope?.launch {
				repoShop.likeStoryInteractions(currentStoryId)
			}
		}
	}

	fun chat(view: View) {
		val context = view.context ?: return

		if (context.isLoginWithOpenAuth()) {
			if (currentStoreWithStories.value?.isSouqMoonStory == true) {
				val it = currentStoreWithStories.value?.chatAgent

				context.launchCometChat {
					uid = it?.uid
					name = it?.name
					avatar = it?.avatar
					status = it?.status
					role = it?.role
				}
			}else {
				context.applicationScope?.launch {
					repoShop.interactionForStoreWhatsApp(currentStoreWithStories.value?.id.orZero())
				}

				currentStoreWithStories.value?.also {
					context.openChatStore(view, it.id.orZero(), it.name.orEmpty(), it.image.orEmpty())
				}
			}
		}
	}

	fun goToStore(view: View) {
		if (currentStoreWithStories.value?.isSouqMoonStory == true) return

		// Mine or Other stores
		userLocalUseCase.goToStoreDetailsIgnoringStoriesCheckIfMyStore(
			view.context ?: return,
			view.findNavController(),
			currentStoreWithStories.value?.id.orZero()
		)
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

			MyLogger.e("udhewihdiewh ch 5 $item == $currentSegment && $play $position")
			if (item == currentSegment && play) {
				animator?.removeAllListeners()
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
						MyLogger.e("udhewihdiewh ch 5 anim end")
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
			animator?.removeAllListeners()
			animator?.cancel()
			play = false
			currentSegment = index
			notifyDataSetChanged()
		}

		fun playCurrentSegment(currentDurationOfAnimationInMs: Long, currentSegment: Int = this.currentSegment) {
			MyLogger.e("udhewihdiewh ch 4 ${this.currentSegment} $currentSegment")
			play = true
			this.currentDurationOfAnimationInMs = currentDurationOfAnimationInMs
			val set = setOf(this.currentSegment, currentSegment)
			this.currentSegment = currentSegment
			notifyItemsChanged(*set.toTypedArray())
		}

		fun pause() {
			//play = false
			animator?.pause()
		}

		fun resume() {
			//play = true
			animator?.resume()
		}

	}

}
