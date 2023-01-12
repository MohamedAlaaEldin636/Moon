package grand.app.moon.extensions.compose

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.databinding.DataBindingUtil
import grand.app.moon.R
import grand.app.moon.core.extenstions.inflateLayout
import grand.app.moon.core.extenstions.layoutInflater
import grand.app.moon.databinding.ItemImageViewBinding
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrEmptyBA
import grand.app.moon.extensions.bindingAdapter.setupWithGlideOrEmptyBAUri

@Composable
fun GlideImageViaXml(
	model: GlideImageViaXmlModel?,
	modifier: Modifier = Modifier,
) {
	AndroidView(modifier = modifier, factory = { context ->
		val binding = DataBindingUtil.inflate<ItemImageViewBinding>(
			context.layoutInflater, R.layout.item_image_view, null, false
		)

		when (model) {
			is GlideImageViaXmlModel.IString -> binding.imageView.setupWithGlideOrEmptyBA(model.string)
			is GlideImageViaXmlModel.IUri -> binding.imageView.setupWithGlideOrEmptyBAUri(model.uri)
			null -> {}
		}

		binding.root
	})
}

sealed interface GlideImageViaXmlModel {
	data class IUri(val uri: Uri?) : GlideImageViaXmlModel
	data class IString(val string: String?) : GlideImageViaXmlModel
}
