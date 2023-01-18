package grand.app.moon.presentation.home

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.dpToPx
import grand.app.moon.databinding.DialogFragmentAnnouncementBinding
import grand.app.moon.databinding.DialogFragmentAppGlobalAnnouncementBinding
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrSplashBA
import grand.app.moon.presentation.base.MADialogFragment
import grand.app.moon.presentation.base.utils.hideLoadingDialog
import grand.app.moon.presentation.base.utils.showLoadingDialog
import grand.app.moon.presentation.home.viewModels.AnnouncementViewModel
import grand.app.moon.presentation.home.viewModels.AppGlobalAnnouncementViewModel
import kotlin.math.roundToInt

@AndroidEntryPoint
class AnnouncementDialogFragment : MADialogFragment<DialogFragmentAnnouncementBinding>() {

	override fun getLayoutId(): Int = R.layout.dialog_fragment_announcement

	override val canceledOnTouchOutside: Boolean = false

	override val heightIsMatchParent: Boolean = true

	private val viewModel by viewModels<AnnouncementViewModel>()

	@CallSuper
	override fun onCreateDialogWindowChanges(window: Window) {
		window.setBackgroundDrawable(
			ColorDrawable(
				ContextCompat.getColor(requireContext(), R.color.announcement_dialog_window_background)
			)
		)
	}

	override fun initializeBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		showLoadingAndData()

		Glide.with(binding.imageView)
			.load(R.drawable.splash)
			.into(binding.imageView)

		Glide.with(binding.imageView)
			.load(viewModel.image.value.orEmpty())
			.addListener(
				object : RequestListener<Drawable?> {
					override fun onLoadFailed(
						e: GlideException?,
						model: Any?,
						target: Target<Drawable?>?,
						isFirstResource: Boolean
					): Boolean {
						hideLoadingAndData()

						return false
					}

					override fun onResourceReady(
						resource: Drawable?,
						model: Any?,
						target: Target<Drawable?>?,
						dataSource: DataSource?,
						isFirstResource: Boolean
					): Boolean {
						hideLoadingAndData()

						return false
					}
				}
			)
			.into(binding.imageView)
	}

	private fun showLoadingAndData() {
		binding.innerConstraintLayout.visibility = View.INVISIBLE
		showLoading()
	}
	private fun hideLoadingAndData() {
		binding.innerConstraintLayout.visibility = View.VISIBLE
		hideLoading()
	}

}
