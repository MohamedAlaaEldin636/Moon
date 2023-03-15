package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.isLoginWithOpenAuth
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemHomeExploreSubsectionBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.shop.ItemExploreInShopInfo
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.serDrawableCompatBA
import grand.app.moon.extensions.bindingAdapter.visibleOrInvisible
import grand.app.moon.presentation.explore.ExploreListFragmentDirections
import grand.app.moon.presentation.home.HomeExploreSubsectionFragment
import grand.app.moon.presentation.home.HomeExploreSubsectionFragmentArgs
import grand.app.moon.presentation.home.OtherStoreDetailsFragment
import grand.app.moon.presentation.home.SimpleUserListOfInteractionsFragment
import grand.app.moon.presentation.home.models.ItemHomeExplore
import grand.app.moon.presentation.myAds.adapter.RVSliderImageFull
import grand.app.moon.presentation.myStore.ExploreInShopInfoFragment
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeExploreSubsectionViewModel @Inject constructor(
	application: Application,
	args: HomeExploreSubsectionFragmentArgs,
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase,
) : AndroidViewModel(application) {

	val initialData = args.jsonOfListOfItemHomeExplore
		.fromJsonInlinedOrNull<List<ItemHomeExplore>>().orEmpty()

	val explores = if (args.maxPageReached == -1) {
		flowOf(PagingData.empty())
	}else {
		repoShop.getHomeExplores(args.maxPageReached.inc())
	}

	var player: ExoPlayer? = null
		private set

	val adapter = RVPagingItemCommonListUsage<ItemHomeExploreSubsectionBinding, ItemHomeExplore>(
		R.layout.item_home_explore_subsection,
		additionalListenersSetups = { adapter, binding ->
			binding.followButtonTextView.setOnClickListener { view ->
				val context = view.context ?: return@setOnClickListener
				val applicationScope = context.applicationScope ?: return@setOnClickListener

				val position = binding.followButtonTextView.tag as? Int ?: return@setOnClickListener

				if (context.isLoginWithOpenAuth()) {
					val store = adapter.snapshot().items.getOrNull(position)?.store ?: return@setOnClickListener
					store.isFollowing = store.isFollowing.orFalse().not()

					applicationScope.launch {
						repoShop.followStore(store.id.orZero())
					}

					adapter.notifyItemChanged(position)
				}
			}

			binding.favImageView.setOnClickListener { view ->
				val context = view.context ?: return@setOnClickListener
				val applicationScope = context.applicationScope ?: return@setOnClickListener

				val position = binding.followButtonTextView.tag as? Int ?: return@setOnClickListener

				if (context.isLoginWithOpenAuth()) {
					val item = adapter.snapshot().items.getOrNull(position) ?: return@setOnClickListener
					item.isLiked = item.isLiked.orFalse().not()
					item.likesCount = item.likesCount.orZero().let {
						if (item.isLiked.orFalse()) it.inc() else it.dec()
					}

					applicationScope.launch {
						repoShop.likeExplore(item.id.orZero())
					}

					adapter.notifyItemChanged(position)
				}
			}

			binding.shareImageView.setOnClickListener { view ->
				val context = view.context ?: return@setOnClickListener
				val applicationScope = context.applicationScope ?: return@setOnClickListener

				val position = binding.followButtonTextView.tag as? Int ?: return@setOnClickListener

				val item = adapter.snapshot().items.getOrNull(position) ?: return@setOnClickListener
				item.sharesCount = item.sharesCount.orZero().inc()

				applicationScope.launch {
					repoShop.shareExplore(item.id.orZero())
				}

				adapter.notifyItemChanged(position)

				context.launchShareText(
					item.shareLink.orEmpty()
				)
				/*context.launchShareText(
					item.store?.name.orEmpty(),
					item.files.orEmpty().joinToString("\n")
				)*/
			}

			val listener = View.OnClickListener { view ->
				//val context = view.context ?: return@OnClickListener
				//val applicationScope = context.applicationScope ?: return@OnClickListener

				val position = binding.followButtonTextView.tag as? Int ?: return@OnClickListener

				val item = adapter.snapshot().items.getOrNull(position) ?: return@OnClickListener

				view.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.commentsListFragment",
					paths = arrayOf(item.id.orZero().toString(), item.commentsCount.orZero().toString(), position.toString())
				)
			}
			binding.addCommentLinearLayout.setOnClickListener(listener)
			binding.commentsCountTextView.setOnClickListener(listener)
			binding.chatImageView.setOnClickListener(listener)

			binding.likesCountTextView.setOnClickListener { view ->
				val context = view.context ?: return@setOnClickListener
				//val applicationScope = context.applicationScope ?: return@setOnClickListener

				val position = binding.followButtonTextView.tag as? Int ?: return@setOnClickListener

				val item = adapter.snapshot().items.getOrNull(position) ?: return@setOnClickListener

				val dataTitle = context.getString(R.string.got_likes_of_var_users, item.likesCount.orZero().toString())

				view.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.simple.user.list.of.interactions",
					paths = arrayOf(
						context.getString(R.string.likes),
						dataTitle,
						SimpleUserListOfInteractionsFragment.Type.EXPLORE_LIKES.toString(),
						item.id.orZero().toString()
					)
				)
			}

			val goToStoreListener = View.OnClickListener { view ->
				val context = view.context ?: return@OnClickListener
				//val applicationScope = context.applicationScope ?: return@OnClickListener

				val position = binding.followButtonTextView.tag as? Int ?: return@OnClickListener

				val item = adapter.snapshot().items.getOrNull(position) ?: return@OnClickListener

				val fragment = view.findFragmentOrNull<HomeExploreSubsectionFragment>() ?: return@OnClickListener

				fragment.setFragmentResultListenerUsingJson<Boolean>(OtherStoreDetailsFragment.KEY_RESULT_IS_FOLLOWING) { isFollowing ->
					adapter.snapshot().items.onEach { it.store?.isFollowing = isFollowing }
					adapter.notifyDataSetChanged()
				}

				userLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
					context,
					view.findNavController(),
					item.store?.id,
					item.store?.toResponseStory()
				)
			}
			binding.storeTextView.setOnClickListener(goToStoreListener)
			binding.storeImageView.setOnClickListener(goToStoreListener)

			binding.videoVolumeImageView.setOnClickListener {
				if (player?.volume.orZero() > 0f) {
					player?.volume = 0f
				}else {
					player?.volume = app.getCurrentDeviceVolume()
				}
				binding.videoVolumeImageView.setImageResource(
					if (player?.volume.orZero() > 0f) R.drawable.volume_unmute else R.drawable.volume_mute
				)
			}
			binding.videoFullscreenImageView.setOnClickListener { view ->
				pausePlayer()

				val position = binding.followButtonTextView.tag as? Int ?: return@setOnClickListener
				val item = adapter.snapshot().items.getOrNull(position) ?: return@setOnClickListener

				view.findNavController().navigateDeepLinkWithoutOptions(
					"fragment-dest",
					"grand.app.moon.dest.show.images.or.video",
					paths = arrayOf(false.toString(), item.files.toJsonInlinedOrNull().orEmpty())
				)
			}
		},
	) { binding, position, item ->
		binding.followButtonTextView.tag = position

		val context = binding.root.context ?: return@RVPagingItemCommonListUsage

		binding.followButtonTextView.serDrawableCompatBA(
			start = if (item.store?.isFollowing.orFalse()) null else ContextCompat.getDrawable(context, R.drawable.follow_add)
		)
		binding.followButtonTextView.text = if (item.store?.isFollowing.orFalse()) context.getString(R.string.un_follow) else context.getString(R.string.follow)

		binding.storeImageView.setupWithGlide {
			load(item.store?.logo).saveDiskCacheStrategyAll()
		}

		binding.storeTextView.text = item.store?.name

		val isVideo = item.isVideo
		binding.videoVolumeImageView.isVisible = isVideo
		binding.videoVolumeImageView.setImageResource(
			if (player?.volume.orZero() > 0f) R.drawable.volume_unmute else R.drawable.volume_mute
		)
		binding.videoFullscreenImageView.isVisible = isVideo
		binding.playerView.visibility = if (isVideo) View.VISIBLE else View.GONE
		binding.sliderView.visibleOrInvisible(isVideo.not())
		binding.playerView.player = null
		if (isVideo.not()) {
			binding.sliderView.setSliderAdapter(RVSliderImageFull(item.files.orEmpty()) { link ->
				setupWithGlide {
					load(link).saveDiskCacheStrategyAll()
				}
			})
			binding.sliderView.startAutoCycle()
		}

		binding.favImageView.setImageResource(
			if (item.isLiked.orFalse()) R.drawable.explore_subsection_is_fav else R.drawable.explore_subsection_is_not_fav
		)

		binding.likesCountTextView.text = "${item.likesCount.orZero()} ${context.getString(R.string.like)}"
		binding.sharesCountTextView.text = "${item.sharesCount.orZero()} ${context.getString(R.string.share)}"

		binding.commentsCountTextView.text = context.getString(R.string.show_all_var_comments, item.commentsCount.orZero())
	}

	fun playVideo(context: Context, videoLink: String): Player? {
		releasePlayer()
		player = ExoPlayer.Builder(context).build().also { exoPlayer ->
			val mediaItem = MediaItem.fromUri(videoLink)
			exoPlayer.setMediaItem(mediaItem)

			exoPlayer.addListener(object : Player.Listener {
				override fun onPlaybackStateChanged(playbackState: Int) {
					if (playbackState == ExoPlayer.STATE_ENDED) {
						player?.seekTo(0L)
					}
				}
			})

			exoPlayer.volume = 0f
			exoPlayer.playWhenReady = true
			exoPlayer.prepare()
		}

		return player
	}

	fun releasePlayer() {
		player?.release()
		player = null
	}

	fun pausePlayer() {
		player?.pause()
	}

	fun resumePlayer() {
		player?.play()
	}

	override fun onCleared() {
		releasePlayer()

		super.onCleared()
	}

}
