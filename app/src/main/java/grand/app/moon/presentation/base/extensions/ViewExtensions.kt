package grand.app.moon.presentation.base.extensions

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.media.browse.MediaBrowser

import com.google.android.exoplayer2.MediaItem


import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.webkit.URLUtil
import android.widget.*
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.NavDeepLinkRequest.Builder.Companion.fromUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import grand.app.moon.R
import java.io.File
import java.lang.Exception
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import grand.app.moon.core.extenstions.isEnglish
import grand.app.moon.domain.utils.SpannedGridLayoutManager
import grand.app.moon.presentation.media.image.utils.ImageMatrixTouchHandler


fun View.show() {
  if (visibility == View.VISIBLE) return

  visibility = View.VISIBLE
  if (this is Group) {
    this.requestLayout()
  }
}

fun View.hide() {
  if (visibility == View.GONE) return

  visibility = View.GONE
  if (this is Group) {
    this.requestLayout()
  }
}

private val TAG = "ViewExtensions"

@BindingAdapter("width")
fun viewWidth(view: View, widthPercent: Int) {
  Log.d(TAG, "viewWidth: HEY")
  if (widthPercent < 100) {
    Log.d(TAG, "viewWidth: ${widthPercent}")
    val total = Resources.getSystem().displayMetrics.widthPixels
    view.layoutParams.width = (total * widthPercent) / 100
  }
}

@BindingAdapter("widthSquare")
fun widthSquare(view: View, count: Int) {
  Log.d(TAG, "viewWidth: HEY")
  val width = Resources.getSystem().displayMetrics.widthPixels / count
  view.layoutParams.width = width
  view.layoutParams.height = width
}


fun View.invisible() {
  if (visibility == View.INVISIBLE) return

  visibility = View.INVISIBLE
  if (this is Group) {
    this.requestLayout()
  }
}

@BindingAdapter("app:fromHtml")
fun TextView.fromHtml(text: String?) {
  if (text != null)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY))
      };
    }
}

@BindingAdapter("app:goneUnless")
fun View.goneUnless(visible: Boolean) {
  visibility = if (visible) View.VISIBLE else View.GONE
  if (this is Group) {
    this.requestLayout()
  }
}

//@BindingAdapter("app:loading")
//fun StoriesProgressView.story(value: Boolean?) {
//  value?.let {
//    when(it){
//      true -> pause()
//      else -> resume()
//    }
//  }
//}


@BindingAdapter("app:rate")
fun RatingBar.rateApp(value: String?) {
  value?.let {
    if (value.isNotEmpty())
      rating = value.toFloat()
  }
}

fun ImageView.drawCircle(backgroundColor: String, borderColor: String? = null) {
  val shape = GradientDrawable()
  shape.shape = GradientDrawable.OVAL
  shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

  shape.setColor(Color.parseColor(backgroundColor))

  borderColor?.let {
    shape.setStroke(10, Color.parseColor(it))
  }

  background = shape
}

fun ImageView.setTint(@ColorRes id: Int) =
  setColorFilter(ContextCompat.getColor(context, id), PorterDuff.Mode.SRC_IN)

fun View.enable() {
  isEnabled = true
  alpha = 1f
}

fun View.disable() {
  isEnabled = false
  alpha = 0.5f
}

fun View.showSnackBar(
  message: String,
  retryActionName: String? = null,
  action: (() -> Unit)? = null
) {
  val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)

  action?.let {
    snackBar.setAction(retryActionName) {
      it()
    }
  }

  snackBar.show()
}

@BindingAdapter(
  value = ["app:loadImage", "app:progressBar", "app:defaultImage"],
  requireAll = false
)
fun ImageView.loadImage(imageUrl: String?, progressBar: ProgressBar?, defaultImage: Any?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    if (URLUtil.isValidUrl(imageUrl)) {
      val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .scale(Scale.FIT)
        .crossfade(true)
        .crossfade(400)
        .error(R.drawable.bg_no_image)
        .target(
          onStart = { placeholder ->
            progressBar?.show()
            setImageDrawable(placeholder)
          },
          onSuccess = { result ->
            progressBar?.hide()
            setImageDrawable(result)
          }
        )
        .listener(onError = { request: ImageRequest, _: Throwable ->
          progressBar?.hide()
          setImageDrawable(request.error)
        })
        .build()

      ImageLoader(context).enqueue(request)
    } else {
      load(File(imageUrl)) {
        crossfade(750) // 75th percentile of a second
        build()
      }
    }

  } else {
    progressBar?.hide()
    try {
      when (defaultImage) {
        null -> setImageResource(R.drawable.bg_no_image)
        is Int -> setImageResource(defaultImage)
        is Drawable -> setImageDrawable(defaultImage)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}


@BindingAdapter(
  value = ["app:imageZoom", "app:progressBar"],
  requireAll = false
)
fun AppCompatImageView.imageZoom(imageUrl: String?, progressBar: ProgressBar?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    if (URLUtil.isValidUrl(imageUrl)) {
      val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .crossfade(true)
        .crossfade(400)
        .error(R.drawable.bg_no_image)
        .target(
          onStart = { placeholder ->
            progressBar?.show()
            setImageDrawable(placeholder)
          },
          onSuccess = { result ->
            progressBar?.hide()
            setImageDrawable(result)
          }
        )
        .listener(onError = { request: ImageRequest, _: Throwable ->
          progressBar?.hide()
          setImageDrawable(request.error)
        })
        .build()

      ImageLoader(context).enqueue(request)
      setOnTouchListener(ImageMatrixTouchHandler(context))

    } else {
      load(File(imageUrl)) {
        crossfade(750) // 75th percentile of a second
        build()
      }
    }

  }
}


@BindingAdapter(
  value = ["app:loadVideo", "app:progressBar"],
  requireAll = false
)
fun PlayerView.loadVideo(videoUrl: String?, progressBar: ProgressBar?) {
  if (videoUrl != null && videoUrl.isNotEmpty()) {
    if (URLUtil.isValidUrl(videoUrl)) {
      val allocator = DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE)

      val loadControl: LoadControl = DefaultLoadControl.Builder()
        .setAllocator(DefaultAllocator(true, 16))
        .setBufferDurationsMs(
          2000,
          5000,
          1500,
          2000
        )
        .setTargetBufferBytes(-1)
        .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl()


      val mediaDataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
        context,
        Util.getUserAgent(context, "mediaPlayerSample")
      )
      val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
        .createMediaSource(MediaItem.fromUri(videoUrl))
      val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

      val player = SimpleExoPlayer.Builder(context)
        .setMediaSourceFactory(mediaSourceFactory)
        .setLoadControl(loadControl)
        .build()
      player.addMediaSource(mediaSource)
      player.prepare()
      hideController()
      useController = false
      setPlayer(player)
      player.volume = 0f
      player.play()
      player.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
          if (state == ExoPlayer.STATE_BUFFERING) {
            progressBar?.show()
          } else {
            progressBar?.hide()
          }
        }
      })
    }
  }
}


@BindingAdapter("app:move")
fun AppCompatTextView.move(dimentions: Float) {
  Log.d(TAG, "moveAnimation: ")
//  startAnimation(AnimationUtils.loadAnimation(context, R.anim.move));
  val bounds = Rect()
  val textPaint: Paint = paint
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    textPaint.getTextBounds(text, 0, text.length, bounds)
  }
  val width: Int = bounds.width()

  if (width > dimentions) {
    val animator: ValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
    animator.repeatCount = ValueAnimator.INFINITE
    animator.interpolator = LinearInterpolator()
    animator.duration = 7000L
    animator.addUpdateListener { animation ->
      val progress = animation.animatedValue as Float
      val width: Int = width
      val translationX = width * progress
      setTranslationX(translationX)

    }
    animator.start()
  }

}

@BindingAdapter(
  value = ["app:loadImageExplore", "app:progressBar", "app:defaultImage", "app:exploreHeight"],
  requireAll = false
)
fun AppCompatImageView.loadImageExplore(
  imageUrl: String?,
  progressBar: ProgressBar?,
  defaultImage: Any?,
  position: Int
) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    if (URLUtil.isValidUrl(imageUrl)) {
      progressBar?.show()
      Glide.with(this)
        .load(imageUrl)
        .listener(object : RequestListener<Drawable> {
          override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
          ): Boolean {
            progressBar?.hide()
            return false
          }

          override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: com.bumptech.glide.load.DataSource?,
            isFirstResource: Boolean
          ): Boolean {
            progressBar?.hide()
            return false
          }

        })
        .into(this)
    } else {
      load(File(imageUrl)) {
        crossfade(750) // 75th percentile of a second
        build()
      }
    }

  } else {
    progressBar?.hide()
    when (defaultImage) {
      null -> setImageResource(R.drawable.bg_no_image)
      is Int -> setImageResource(defaultImage)
      is Drawable -> setImageDrawable(defaultImage)
    }
  }
//  if (position % 3 == 0) {
//    layoutParams.height = resources.getDimension(R.dimen.dimen300).toInt()
//  } else
//    layoutParams.height = resources.getDimension(R.dimen.dimen150).toInt()
}

@BindingAdapter(value = ["app:loadCircleImage", "app:progressBar"], requireAll = false)
fun ImageView.loadCircleImage(imageUrl: String?, progressBar: ProgressBar?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    val request = ImageRequest.Builder(context)
      .data(imageUrl)
      .crossfade(true)
      .crossfade(400)
      .placeholder(R.color.backgroundGray)
      .error(R.drawable.bg_no_image)
      .transformations(
        CircleCropTransformation()
      )
      .target(
        onStart = { placeholder ->
          progressBar?.show()
          setImageDrawable(placeholder)
        },
        onSuccess = { result ->
          progressBar?.hide()
          setImageDrawable(result)
        }
      )
      .listener(onError = { request: ImageRequest, _: Throwable ->
        progressBar?.hide()
        setImageDrawable(request.error)
      })
      .build()

    ImageLoader(context).enqueue(request)
  } else {
    progressBar?.hide()

    load(R.drawable.bg_no_image) {
      crossfade(true)
      transformations(
        CircleCropTransformation()
      )
    }
  }
}

@BindingAdapter(value = ["app:loadRoundImage", "app:progressBar"], requireAll = false)
fun ImageView.loadRoundImage(imageUrl: String?, progressBar: ProgressBar?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    val request = ImageRequest.Builder(context)
      .data(imageUrl)
      .crossfade(true)
      .crossfade(400)
      .placeholder(R.color.backgroundGray)
      .error(R.drawable.bg_no_image)
      .transformations(
        RoundedCornersTransformation(
          resources.getDimension(R.dimen.dimen7)
        )
      )
      .target(
        onStart = { placeholder ->
          progressBar?.show()
          setImageDrawable(placeholder)
        },
        onSuccess = { result ->
          progressBar?.hide()
          setImageDrawable(result)
        }
      )
      .listener(onError = { request: ImageRequest, _: Throwable ->
        progressBar?.hide()
        setImageDrawable(request.error)
      })
      .build()

    ImageLoader(context).enqueue(request)
  } else {
    progressBar?.hide()

    load(R.drawable.bg_no_image) {
      crossfade(true)
      transformations(
        RoundedCornersTransformation(
          resources.getDimension(R.dimen.dimen7)
        )
      )
    }
  }
}

@BindingAdapter("premium")
fun loadPremium(imageView: ImageView,isPremium : Int) {
  if(isPremium == 0)
    imageView.visibility = View.GONE
  else {
    if (imageView.context.isEnglish()) {
      imageView.setImageResource(R.drawable.premium)
    } else
      imageView.setImageResource(R.drawable.special)
  }
}


@BindingAdapter("load_drawable")
fun loadDrawable(imageView: ImageView, drawable: Drawable?) {
  imageView.setImageDrawable(drawable)
}


@BindingAdapter("app:adapter", "app:span", "app:orientation")
fun getItemsV2Binding(
  recyclerView: RecyclerView,
  itemsAdapter: RecyclerView.Adapter<*>?,
  spanCount: String,
  orientation: String
) {
  if (orientation == "1") initVerticalRV(
    recyclerView,
    recyclerView.context,
    spanCount.toInt()
  ) else initHorizontalRV(recyclerView, recyclerView.context, spanCount.toInt())
  recyclerView.adapter = itemsAdapter
}

@BindingAdapter("app:adapterGallery")
fun adapterGallery(
  recyclerView: RecyclerView,
  itemsAdapter: RecyclerView.Adapter<*>?,
) {

  val manager = SpannedGridLayoutManager(
    object : SpannedGridLayoutManager.GridSpanLookup {
      override fun getSpanInfo(position: Int): SpannedGridLayoutManager.SpanInfo {
        var x = 0
        if (position % 9 == 0) {
          x = position / 9
        }

        return when {
          position == 1 || x % 2 == 1 || (position - 1) % 18 == 0 ->
            SpannedGridLayoutManager.SpanInfo(2, 2)
          else ->
            SpannedGridLayoutManager.SpanInfo(1, 1)
        }

      }
    },
    3,  // number of columns
    1f // how big is default item
  )
  recyclerView.layoutManager = manager
  recyclerView.adapter = itemsAdapter
}


@SuppressLint("WrongConstant")
fun initVerticalRV(recyclerView: RecyclerView, context: Context?, spanCount: Int) {
  recyclerView.setHasFixedSize(true)
  recyclerView.setItemViewCacheSize(30)
  recyclerView.isDrawingCacheEnabled = true
  recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
  recyclerView.layoutManager =
    GridLayoutManager(context, spanCount, LinearLayoutManager.VERTICAL, false)
}

@SuppressLint("WrongConstant")
fun initHorizontalRV(recyclerView: RecyclerView, context: Context?, spanCount: Int) {
  recyclerView.setHasFixedSize(true)
  recyclerView.setItemViewCacheSize(30)
  recyclerView.isDrawingCacheEnabled = true
  recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
  recyclerView.layoutManager =
    GridLayoutManager(context, spanCount, LinearLayoutManager.HORIZONTAL, false)
}