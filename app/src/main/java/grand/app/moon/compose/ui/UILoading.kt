package grand.app.moon.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import grand.app.moon.compose.ExtendedTheme
import kotlinx.coroutines.delay

object UILoading {

	@Composable
	fun TmpScreen(delayInMs: Long = 250, showLoading: Boolean = true, actualScreen: @Composable () -> Unit) {
		var showActualScreen by remember {
			mutableStateOf(false)
		}

		if (showActualScreen) {
			actualScreen()
		}else {
			Box(
				Modifier
					.fillMaxSize()
					.background(ExtendedTheme.colors.dialogScrim)
			) {
				if (showLoading) {
					AndroidView(
						modifier = Modifier.size(60.dp).align(Alignment.Center),
						factory = {
							LottieAnimationView(it).apply {
								setAnimation("loading.json")
								repeatCount = LottieDrawable.INFINITE
								playAnimation()
							}
						}
					)
				}
			}

			LaunchedEffect(Unit) {
				delay(delayInMs)

				showActualScreen = true
			}
		}
	}

	/*
	<com.airbnb.lottie.LottieAnimationView
    android:id="@+id/pb_base_loading_bar"
    android:layout_width="@dimen/dimen60"
    android:layout_height="@dimen/dimen60"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:lottie_autoPlay="true"
    app:lottie_fileName="loading.json"
    app:lottie_loop="true" />
	 */

}
