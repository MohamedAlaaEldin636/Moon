package grand.app.moon.extensions.bindingAdapter

import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter("lottieAnimationView_setAnimationFromUrlBA")
fun LottieAnimationView.setAnimationFromUrlBA(link: String?) {
	if (link.isNullOrEmpty().not()) {
		setAnimationFromUrl(link)
	}
}

@BindingAdapter("lottieAnimationView_setAnimationFileNameBA")
fun LottieAnimationView.setAnimationFileNameBA(name: String?) {
	setAnimation(name)
}
