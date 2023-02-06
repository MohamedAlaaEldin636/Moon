package grand.app.moon.presentation.home.viewModels

import android.app.Application
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemHomeExploreBinding
import grand.app.moon.databinding.ItemHomeRvBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.home.HomeExploreFragment
import grand.app.moon.presentation.home.models.ItemHomeExplore
import grand.app.moon.presentation.home.models.ItemHomeRV
import javax.inject.Inject

@HiltViewModel
class HomeExploreViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val homeUseCase: HomeUseCase,
	val userLocalUseCase: UserLocalUseCase
) : AndroidViewModel(application) {

	val explores = repoShop.getHomeExplores()

	// todo share of inner screen is share of file link ex. video or image isa.
	val adapter = RVPagingItemCommonListUsageWithExoPlayer<ItemHomeExploreBinding, ItemHomeExplore>(
		R.layout.item_home_explore,
		onItemClick = { adapter, binding ->
			val item = (binding.constraintLayout.tag as? String).fromJsonInlinedOrNull<ItemHomeExplore>()

			General.TODO("not programmed yet isa. ${item?.isVideo}")
		},
		onViewRecycledAction = {
			it.itemView.findViewById<ImageView>(R.id.imageImageView).clearWithGlide()
		}
	) { binding, position, item, viewHolder ->
		val context = binding.root.context ?: return@RVPagingItemCommonListUsageWithExoPlayer

		binding.constraintLayout.tag = item.toJsonInlinedOrNull()

		val isVideo = item.isVideo

		viewHolder.releasePlayer()
		binding.playerView.player = null
		binding.playerView.isVisible = false
		binding.imageImageView.isVisible = false

		binding.indicatorImageView.setImageResource(
			when {
				isVideo -> R.drawable.video_indicator
				item.files.orEmpty().size > 1 -> R.drawable.multi_image_indicator
				else -> 0
			}
		)

		binding.likeTextView.text = item.likesCount.orZero().toString()

		binding.chatTextView.text = item.commentsCount.orZero().toString()

		/*if (isVideo) {
			binding.imageImageView.setupWithGlide {
				load(item.files?.firstOrNull()).asVideo()
			}

			val videoLink = item.files?.firstOrNull()
			if (false && videoLink != null && videoLink.isNotEmpty()) {
				viewHolder.player = ExoPlayer.Builder(context).build().also { exoPlayer ->
					val mediaItem = MediaItem.fromUri(videoLink)
					exoPlayer.setMediaItem(mediaItem)

					exoPlayer.addListener(object : Player.Listener {
						override fun onPlaybackStateChanged(playbackState: Int) {
							if (playbackState == ExoPlayer.STATE_READY) {
								viewHolder.getBindingOrNull()?.imageImageView?.isVisible = false
							}else if (playbackState == ExoPlayer.STATE_ENDED) {
								viewHolder.player?.seekTo(0L)
							}
						}
					})

					exoPlayer.volume = 0f
					exoPlayer.playWhenReady = true
					exoPlayer.prepare()
				}
				binding.playerView.player = viewHolder.player
			}
		}else {
			binding.imageImageView.setupWithGlide {
				load(item.files?.firstOrNull())
			}
		}*/
	}

	fun goToAddExplore(view: View) {
		val fragment = view.findFragmentOrNull<HomeExploreFragment>() ?: return

		if (userLocalUseCase().isStore.orFalse().not()) {
			fragment.findNavController().navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.my.become.shop.package"
			)

			return
		}

		fragment.handleRetryAbleActionCancellable(
			action = {
				homeUseCase.checkAvailableExplore()
			}
		) {
			if (it > 0) {
				fragment.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.add.explore"
				)
			}else {
				fragment.showError(fragment.getString(R.string.no_more_rem_explore_in_your_package))

				fragment.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.my.become.shop.package"
				)
			}
		}
	}

	fun setupRvs(
		binding: ItemHomeExploreBinding,
		item: ItemHomeExplore,
		position: Int,
		viewHolder: VHPagingItemCommonListUsageWithExoPlayer<ItemHomeExploreBinding, ItemHomeExplore>
	) {
		binding.imageImageView.isVisible = true

		val context = binding.root.context ?: return

		val isVideo = item.isVideo

		if (isVideo) {
			binding.imageImageView.setupWithGlide {
				load(item.files?.firstOrNull()).asVideo()
			}

			val videoLink = item.files?.firstOrNull()
			if (false && videoLink != null && videoLink.isNotEmpty()) {
				viewHolder.player = ExoPlayer.Builder(context).build().also { exoPlayer ->
					val mediaItem = MediaItem.fromUri(videoLink)
					exoPlayer.setMediaItem(mediaItem)

					exoPlayer.addListener(object : Player.Listener {
						override fun onPlaybackStateChanged(playbackState: Int) {
							if (playbackState == ExoPlayer.STATE_READY) {
								viewHolder.getBindingOrNull()?.imageImageView?.isVisible = false
								viewHolder.getBindingOrNull()?.playerView?.isVisible = true
								viewHolder.getBindingOrNull()?.playerView?.player = viewHolder.player
							}else if (playbackState == ExoPlayer.STATE_ENDED) {
								viewHolder.player?.seekTo(0L)
							}
						}
					})

					exoPlayer.volume = 0f
					exoPlayer.playWhenReady = true
					exoPlayer.prepare()
				}
			}
		}else {
			binding.imageImageView.setupWithGlide {
				load(item.files?.firstOrNull())
			}
		}
	}

}
