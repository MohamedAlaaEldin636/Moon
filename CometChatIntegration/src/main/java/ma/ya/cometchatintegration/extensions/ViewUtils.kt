package ma.ya.cometchatintegration.extensions

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import ma.ya.cometchatintegration.R

fun View.makeInvisible() {
	visibility = View.INVISIBLE
}
fun View.show() {
	isVisible = true
}
fun View.hide() {
	isVisible = false
}

fun <Item> View.showPopup(
	list: List<Item>,
	context: Context = this.context,
	itemTransformation: (Item) -> String = { it.toStringOrEmpty() },
	onDismissListener: () -> Unit = {},
	onMenuItemClickListener: (MenuItem) -> Unit = {},
) {
	val popupMenu = PopupMenu(context, this)

	popupMenu.inflate(R.menu.menu_empty)
	for (item in list) {
		popupMenu.menu.add(itemTransformation(item))
	}

	popupMenu.setOnMenuItemClickListener {
		onMenuItemClickListener(it)

		false
	}

	popupMenu.setOnDismissListener {
		onDismissListener()
	}
	popupMenu.show()
}
