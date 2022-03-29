package grand.app.moon.helpers

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import java.util.ArrayList

class PopUpMenuHelper {
  var popUpMenu: PopupMenu? = null
  fun openPopUp(
    context: Context,
    viewAction: View?,
    objects: ArrayList<String>?,
    callBack: (result: Int) -> Unit
  ) {
    val list: MutableList<String> = ArrayList()
    list.clear()
    list.addAll(objects!!)
    popUpMenu = PopupMenu(context, viewAction!!)
    for (i in list.indices) {
      popUpMenu!!.menu.add(i, i, i, list[i])
    }
    popUpMenu!!.setOnMenuItemClickListener { item ->
      callBack(item.itemId)
      false
    }
    popUpMenu!!.show()
  }
}