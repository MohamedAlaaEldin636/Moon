package grand.app.moon.helpers.paging

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


@BindingAdapter("rv_adapterPagination")
fun loadPaginationRecyclerView(recyclerView: RecyclerView,
                               adapter: PagingDataAdapter<*, *>?) {
  var currentlyRefreshing = false
  recyclerView.layoutManager = LinearLayoutManager(
    recyclerView.context,
    LinearLayoutManager.VERTICAL, false
  )
  recyclerView.adapter = adapter
  adapter?.addLoadStateListener { loadStates ->
    if (loadStates.refresh == LoadState.Loading ||
      loadStates.append == LoadState.Loading) {
      currentlyRefreshing = true
    } else if (currentlyRefreshing) {
      currentlyRefreshing = false

      recyclerView.post {
        recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount.dec())
      }
    }
  }

}

