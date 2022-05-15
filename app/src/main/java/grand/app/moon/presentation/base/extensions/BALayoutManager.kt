package grand.app.moon.presentation.base.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * @param percentage from 1 to 99
 */
@BindingAdapter("rv_ln","adapter_percentage")
fun loadDrawable(recyclerView: RecyclerView, percentage: Int,adapter: RecyclerView.Adapter<*>?) {
  val l = object : LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false) {
    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
      if (lp != null) {
        lp.width = (width.toFloat() * (percentage.toFloat() / 100f)).roundToInt()
      }

      return super.checkLayoutParams(lp)
    }
  }
  recyclerView.layoutManager = l
  recyclerView.adapter = adapter
}


@BindingAdapter("rv_vertical","adapter_percentage","span")
fun loadRecyclerView(recyclerView: RecyclerView, percentage: Int,adapter: RecyclerView.Adapter<*>?,span: Int?) {
  val l = object : GridLayoutManager(recyclerView.context, span!!) {
    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
      if (lp != null) {
        lp.width = (width.toFloat() * (percentage.toFloat() / 100f)).roundToInt()
      }

      return super.checkLayoutParams(lp)
    }
  }
  recyclerView.layoutManager = l
  recyclerView.adapter = adapter
}
