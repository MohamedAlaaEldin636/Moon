package grand.app.moon.compose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import grand.app.moon.compose.ExtendedTheme

object UIText {

	@Composable
	fun TajawalRegularForm(
		text: String,
		textSize: TextUnit = 13.sp,
		modifier: Modifier = Modifier
			.fillMaxWidth()
			.wrapContentHeight(align = Alignment.Top),
		textAlign: TextAlign? = null,
		color: Color? = null
	) {
		Text(
			modifier = modifier,
			text = text,
			style = ExtendedTheme.typography.tajawalRegular.copy(
				fontSize = textSize,
				color = color ?: ExtendedTheme.typography.tajawalRegular.color
			),
			textAlign = textAlign
		)
	}

	@Composable
	fun TajawalMediumForm(
		text: String,
		textSize: TextUnit = 14.sp,
		modifier: Modifier = Modifier
			.fillMaxWidth()
			.wrapContentHeight(align = Alignment.Top),
		textAlign: TextAlign? = null,
		color: Color? = null
	) {
		Text(
			modifier = modifier,
			text = text,
			style = ExtendedTheme.typography.tajawalMedium.copy(
				fontSize = textSize,
				color = color ?: ExtendedTheme.typography.tajawalMedium.color
			),
			textAlign = textAlign
		)
	}

}