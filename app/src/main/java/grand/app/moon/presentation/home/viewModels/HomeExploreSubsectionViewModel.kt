package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
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
import grand.app.moon.extensions.*
import grand.app.moon.extensions.bindingAdapter.serDrawableCompatBA
import grand.app.moon.extensions.bindingAdapter.visibleOrInvisible
import grand.app.moon.presentation.home.HomeExploreSubsectionFragmentArgs
import grand.app.moon.presentation.home.models.ItemHomeExplore
import grand.app.moon.presentation.myAds.adapter.RVSliderImageFull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeExploreSubsectionViewModel @Inject constructor(
	application: Application,
	args: HomeExploreSubsectionFragmentArgs,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val initialData = args.jsonOfListOfItemHomeExplore
		.fromJsonInlinedOrNull<List<ItemHomeExplore>>().orEmpty()

	val explores = repoShop.getHomeExplores(args.maxPageReached.inc())

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

				if (context.isLoginWithOpenAuth()) {
					val item = adapter.snapshot().items.getOrNull(position) ?: return@setOnClickListener
					item.sharesCount = item.sharesCount.orZero().inc()

					applicationScope.launch {
						repoShop.shareExplore(item.id.orZero())
					}

					adapter.notifyItemChanged(position)

					context.launchShareText(
						item.store?.name.orEmpty(),
						item.files.orEmpty().joinToString("\n")
					)
				}
			}

			val listener = View.OnClickListener { view ->
				val context = view.context ?: return@OnClickListener
				val applicationScope = context.applicationScope ?: return@OnClickListener

				val position = binding.followButtonTextView.tag as? Int ?: return@OnClickListener

				val item = adapter.snapshot().items.getOrNull(position) ?: return@OnClickListener

				/* todo
				v.findNavController().navigate(
		ExploreListFragmentDirections.actionExploreListFragmentToCommentsListFragment(
			model.id,
			model.comments,
			position
		)
	)
				 */
			}
			binding.addCommentLinearLayout.setOnClickListener(listener)
			binding.commentsCountTextView.setOnClickListener(listener)
			binding.chatImageView.setOnClickListener(listener)

			binding.likesCountTextView.setOnClickListener {
				// todo ...
				/*
				v.findNavController().navigate(
      R.id.userListFragment,
      bundleOf(
        "explore_id" to model.id,
        "title" to v.resources.getString(R.string.this_was_liked_by_users, model.likes),
        Constants.TabBarText to v.resources.getString(R.string.likes)
      ), Constants.NAVIGATION_OPTIONS
    )
				 */
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
			load(item.store?.logo)
		}

		binding.storeTextView.text = item.store?.name

		val isVideo = item.isVideo
		binding.playerView.isVisible = isVideo
		binding.sliderView.visibleOrInvisible(isVideo.not())
		binding.playerView.player = null
		if (isVideo.not()) {
			binding.sliderView.setSliderAdapter(RVSliderImageFull(item.files.orEmpty()) { link ->
				setupWithGlide {
					load(link)
						.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
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

			//exoPlayer.volume = 0f
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
		resumePlayer()

		super.onCleared()
	}

}
