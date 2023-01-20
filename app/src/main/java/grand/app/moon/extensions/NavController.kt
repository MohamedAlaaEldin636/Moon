package grand.app.moon.extensions

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.*
import androidx.navigation.fragment.findNavController
import grand.app.moon.R

/*fun Fragment.findNavControllerOfProject(): NavController {
	return Navigation.findNavController(
		requireActivity(),
		R.id.mainNavHostFragment
	)
}*/

/*fun View.findNavControllerOfProject(): NavController {
	return findFragment<Fragment>().findNavControllerOfProject()
}*/

fun NavController.popAllBackStacks() {
	while (popBackStack()) {
		continue
	}
}

fun NavController.navigateDeepLinkWithoutOptions(scheme: String, authority: String, vararg paths: String) =
	navigateDeepLinkOptionalOptions(scheme, authority, null, *paths)

fun defaultAnimationsNavOptionsBuilder() = NavOptions.Builder()
	.setEnterAnim(R.anim.anim_slide_in_right)
	.setExitAnim(R.anim.anim_slide_out_left)
	.setPopEnterAnim(R.anim.anim_slide_in_left)
	.setPopExitAnim(R.anim.anim_slide_out_right)

fun NavController.navigateDeepLinkWithOptions(
	scheme: String,
	authority: String,
	options: NavOptions = NavOptions.Builder()
		.setEnterAnim(R.anim.anim_slide_in_right)
		.setExitAnim(R.anim.anim_slide_out_left)
		.setPopEnterAnim(R.anim.anim_slide_in_left)
		.setPopExitAnim(R.anim.anim_slide_out_right)
		.build(),
	vararg paths: String?
) = navigateDeepLinkOptionalOptions(scheme, authority, options, *paths)

private fun NavController.navigateDeepLinkOptionalOptions(
	scheme: String,
	authority: String,
	options: NavOptions?,
	vararg paths: String?
) {
	val uri = Uri.Builder()
		.scheme(scheme)
		.authority(authority)
		.let {
			var builder = it
			for (path in paths) {
				builder = builder.appendPath(path)
			}
			builder
		}
		.build()
	val request = NavDeepLinkRequest.Builder.fromUri(uri).build()

	navigate(request, options)
}

fun NavController.inflateGraph(@NavigationRes navigationRes: Int, args: Bundle? = null, startDestinationId: Int = -1) {
	try {
		graph
	}catch (e: IllegalStateException) {
		val navGraph = navInflater.inflate(navigationRes)
		if (startDestinationId >= 0) {
			navGraph.setStartDestination(startDestinationId)
		}
		setGraph(navGraph, args)
	}
}

/*fun NavController.executeWhenLoadingEndsIfExists(block: () -> Unit) {
	if (getBackStackEntryOrNull(R.id.dest_lottie_loader_dialog) != null) {
		addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
			override fun onDestinationChanged(
				controller: NavController,
				destination: NavDestination,
				arguments: Bundle?
			) {
				if (controller.getBackStackEntryOrNull(R.id.dest_lottie_loader_dialog) != null) {
					removeOnDestinationChangedListener(this)

					block()
				}
			}
		})
	}else {
		block()
	}
}*/

private fun NavController.getBackStackEntryOrNull(destinationId: Int) = kotlin.runCatching {
	getBackStackEntry(destinationId)
}.getOrNull()

inline fun <reified T> NavController.navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
	any: T,
	key: String = AppConsts.NavController.GSON_KEY,
) {
	MyLogger.e("resultssss -> navUpThenSetResultInBackStackEntrySavedStateHandleViaGson $any $key")

	navigateUp()

	currentBackStackEntry?.savedStateHandle?.set(
		key,
		any.toJsonInlinedOrNull().orEmpty()
	)
}

inline fun <reified T> NavController.setResultInPreviousBackStackEntrySavedStateHandleViaGson(
	any: T,
	key: String = AppConsts.NavController.GSON_KEY,
) {
	MyLogger.e("resultssss -> setResultInPreviousBackStackEntrySavedStateHandleViaGson $any $key")

	previousBackStackEntry?.savedStateHandle?.set(
		key,
		any.toJsonInlinedOrNull().orEmpty()
	)
}

inline fun <reified T> AppCompatActivity.observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull(
	navController: NavController,
	key: String = AppConsts.NavController.GSON_KEY,
	noinline onNotNullResult: (T) -> Unit
) {
	navController.currentBackStackEntry?.savedStateHandle?.getLiveData(
		key,
		""
	)?.observe(this) {
		MyLogger.e("resultssss -> observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull on observe $key, $it")

		if (it.isNullOrEmpty().not()) {
			val value = it.fromJsonInlinedOrNull<T>()

			navController.currentBackStackEntry?.savedStateHandle?.set(
				key,
				""
			)

			if (value != null) {
				onNotNullResult(value)
			}
		}
	}
}

inline fun <reified T> Fragment.observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull(
	key: String = AppConsts.NavController.GSON_KEY,
	noinline onNotNullResult: (T) -> Unit
) {
	findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData(
		key,
		""
	)?.observe(viewLifecycleOwner) {
		MyLogger.e("resultssss -> observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull on observe $key, $it")

		if (it.isNullOrEmpty().not()) {
			val value = it.fromJsonInlinedOrNull<T>()

			findNavController().currentBackStackEntry?.savedStateHandle?.set(
				key,
				""
			)

			if (value != null) {
				onNotNullResult(value)
			}
		}
	}
}
