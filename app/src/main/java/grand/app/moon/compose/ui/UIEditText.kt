package grand.app.moon.compose.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.*
import grand.app.moon.compose.ExtendedTheme

fun String.toAnnotatedString() = AnnotatedString(this)
fun String.toTextFieldValue() = TextFieldValue(
	this, selection = TextRange.Zero
)

object UIEditText {

	object WithBorder {
		@Composable
		operator fun invoke(
			textModifier: Modifier,
			boxModifier: Modifier,
			hint: AnnotatedString,
			text: String,
			onTextChange: (String) -> Unit,
			textStyle: TextStyle,
			readOnly: Boolean = false,
			keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
			suffixComposable: (@Composable () -> Unit)? = null,
			onTextLayout: (TextLayoutResult) -> Unit = {},
			contentAlignment: Alignment.Vertical
		) {
			BasicTextField(
				value = text,
				onValueChange = { onTextChange(it) },
				modifier = textModifier,
				decorationBox = {
					Row(
						boxModifier
							.border(0.2.dp, ExtendedTheme.colors.borderTextField, RoundedCornerShape(5.dp))
							.clip(RoundedCornerShape(5.dp))
							.padding(16.dp)
					) {
						Row(
							Modifier
								.weight(1f)
								.align(contentAlignment)) {
							if (text.isEmpty()) {
								Text(
									text = hint,
									style = textStyle.copy(color = ExtendedTheme.colors.hintColor),
								)
							}

							it()
						}

						if (suffixComposable != null) {
							Spacer(modifier = Modifier.width(8.dp))

							Box(Modifier.align(contentAlignment)) {
								suffixComposable()
							}
						}
					}
				},
				textStyle = textStyle,
				readOnly = readOnly,
				keyboardOptions = keyboardOptions,
				onTextLayout = onTextLayout,
			)
		}

		@Composable
		fun TajawalRegularForm(
			hint: String,
			isRequired: Boolean,
			text: String,
			onTextChange: (String) -> Unit,
			textSize: TextUnit = 13.sp,
			textModifier: Modifier = Modifier,
			boxModifier: Modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight(align = Alignment.Top),
			onClick: (() -> Unit)? = null,
			keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
			suffixComposable: (@Composable () -> Unit)? = null,
			onTextLayout: (TextLayoutResult) -> Unit = {},
			contentAlignment: Alignment.Vertical = Alignment.CenterVertically,
			additionalBoxModifier: Modifier = Modifier,
		) {
			val finalBoxModifier = boxModifier.then(additionalBoxModifier)

			val hintText = "$hint${if (isRequired) " *" else ""}"
			invoke(
				hint = AnnotatedString(
					hintText,
					listOfNotNull(
						if (!isRequired) null else AnnotatedString.Range(
							SpanStyle(
								color = ExtendedTheme.colors.requiredColor
							),
							hintText.lastIndex,
							hintText.length
						)
					)
				),
				text = text,
				onTextChange = {
					onTextChange(it)
				},
				textModifier = textModifier,
				boxModifier = finalBoxModifier.let {
					if (onClick == null) it else it.clickable(onClick = onClick)
				},
				textStyle = ExtendedTheme.typography.tajawalRegular.copy(
					fontSize = textSize,
				),
				readOnly = onClick != null,
				keyboardOptions = keyboardOptions,
				suffixComposable = suffixComposable,
				onTextLayout = onTextLayout,
				contentAlignment = contentAlignment
			)
		}
	}
	
}
