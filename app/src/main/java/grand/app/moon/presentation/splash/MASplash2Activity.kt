package grand.app.moon.presentation.splash

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.core.extenstions.InitialAppLaunch
import grand.app.moon.core.extenstions.getInitialAppLaunch
import grand.app.moon.core.makeAppInitializations
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ActivityMaSplash2Binding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.intro.IntroActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

// done problem of launching application white screen but still takes time and to fix it will
// requires a lot of debugging which will require days or weeks of it and delay done after showing ui
/**
 * 1. load app initializations as a lazy initialization from here as here is always the first
 * entry point
 * 2. load in background in app scope the categories and save them to cache them isa.
 * 3. check app state and decide next screen which will either be intro activity or home one.
 */
@AndroidEntryPoint
class MASplash2Activity : AppCompatActivity() {

	@Inject
	lateinit var repoShop: RepoShop

	fun getFileFromAssets(context: Context, fileName: String): File = File(context.cacheDir, fileName)
		.also {
			if (!it.exists()) {
				it.outputStream().use { cache ->
					context.assets.open(fileName).use { inputStream ->
						inputStream.copyTo(cache)
					}
				}
			}
		}

	//Uri url = Uri.parse("file:///android_asset/folder1/video.mp4");
	fun ExoPlayer.prepareExoPlayerFromAssetResource(uri: String = "file:///android_asset/splash_video.mp4") {
		/*val dataSourceFactory = DataSource.Factory {
			AssetDataSource(this@MASplash2Activity)
		}
		val newUri = "android.resource://" + packageName + "/" + R.raw.splash_video_2
		val mediaSource = ProgressiveMediaSource
			.Factory(dataSourceFactory)
			.createMediaSource(MediaItem.fromUri(Uri.parse(newUri)))

		DefaultExtractorsFactory()
		val ms = DefaultMediaSourceFactory(dataSourceFactory).createMediaSource(MediaItem.fromUri(newUri))

		kotlin.runCatching {
			//resources.getIdentifier()
			val url0 = Uri.parse("file:///android_asset/splash_video_2.mp4")
			val file0 = File(url0.toString())
			MyLogger.e("sssssssssss 0 -> ${file0.exists()} ${file0.canRead()}")
			val url1 = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash_video_2)
			val file1 = File(url1.toString())
			MyLogger.e("sssssssssss ${file1.exists()} ${file1.canRead()}")
			val filePath =  getFileFromAssets(this@MASplash2Activity, "abc.mp4")
			MyLogger.e("sssssssssss chhhhhhhhhhhhhhh 0")
			val url2 = Uri.fromFile(filePath)
			MyLogger.e("sssssssssss chhhhhhhhhhhhhhh 1")
			val file2 = File(url2.toString())
			MyLogger.e("sssssssssssAAAAAAAAAA ${file2.exists()} ${file2.canRead()} ${filePath.exists()} ${filePath.canRead()}")
		}.getOrElse {
			MyLogger.e("sssssssssss error ${it.message} $it")
		}

		setMediaSource(ms)
		playWhenReady = true
		prepare()
		play()*/

		val uris = "android.resource://" + packageName + "/" + R.raw.splash_video_2
		val uri = Uri.parse(uris)
		val mediaItem = MediaItem.fromUri(uri)
		setMediaItem(mediaItem)
		prepare()
		play()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val binding = DataBindingUtil.setContentView<ActivityMaSplash2Binding>(this, R.layout.activity_ma_splash_2)

		val appLinkData = intent?.data ?: NotificationsUtils.getModelAsJsonAsUriFromIntent(intent)
		MyApplication.deepLinkUri = appLinkData
		MyApplication.usedDeepLink = appLinkData == null
		MyLogger.e("heeeeeeeeeeeey ${MyApplication.usedDeepLink} ${intent.getStringExtra(NotificationsUtils.INTENT_EXTRA_KEY_MODEL_AS_JSON)} $appLinkData")

		binding.splashImageView.setupWithGlide {
			load(R.drawable.aaaa).saveDiskCacheStrategyAll()
		}

		// splash_giiif.gif todo
		/*Glide.with(this).asGif().load(R.raw.splash_giiif).into(binding.gifImageView)
		*//*binding.gifImageView.setupWithGlide {
			load(R.raw.splash_giiif)
		}*//*

		// Create a new SimpleExoPlayer instance
		val player = ExoPlayer.Builder(this).build()

		// Set the player for the player view
		binding.playerView.player = player

		player.addListener(object : Player.Listener {
			override fun onPlayerError(error: PlaybackException) {
				MyLogger.e("errrrr ${error.message} $error")
			}
		})*/

		//player.prepareExoPlayerFromAssetResource()

		// Prepare the player with the video source
		/*player.playWhenReady = true
		player.prepare()*/

		binding.root.post {
			lifecycleScope.launch {
				delay(250)

				makeAppInitializations()

				delay(50)

				applicationScope?.launch {
					if (getInitialAppLaunch() == InitialAppLaunch.SHOW_HOME) {
						repoShop.fetchAnnouncementAndSaveItLocally()
					}

					repoShop.fetchAllCategoriesAndSaveThemLocallyIfPossible()
				}

				val jClass = when (getInitialAppLaunch()) {
					InitialAppLaunch.SHOW_WELCOMING_SCREENS -> IntroActivity::class.java
					InitialAppLaunch.SHOW_HOME -> HomeActivity::class.java
				}

				//delay(10_000)

				openActivityAndClearStack(jClass)
			}
		}
	}

}
