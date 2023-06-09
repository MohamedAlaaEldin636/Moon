package grand.app.moon.helpers.map.cluster

import android.util.Log
import android.widget.Checkable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import grand.app.moon.domain.home.models.Store
import grand.app.moon.extensions.orZero

class ClusterCustomItem(story: Store, posArray: Int) : ClusterItem,
  Checkable {
  private val position: LatLng
  private val title: String
  private val snippet: String
  private val store: Store
  val posArray: Int
  private var isChecked = false
  override fun getPosition(): LatLng {
    return position
  }

  override fun getTitle(): String? {
    return title
  }

  override fun getSnippet(): String? {
    return snippet
  }

  fun getStore(): Store {
    return store
  }

  override fun setChecked(b: Boolean) {
    isChecked = b
  }

  fun getChecked(): Boolean {
    return isChecked
  }

  override fun isChecked(): Boolean {
    return isChecked
  }

  override fun toggle() {}
  fun setUpdateMarker(place: ClusterCustomItem?) {}

  companion object {
    private const val TAG = "ClusterCustomItem"
  }

  init {
    position = LatLng(story.latitude.orZero(), story.longitude.orZero())
    Log.d(TAG, "cluster: ${story.name} , ${story.nickname}")
    title = if (story.name != "") story.name.orEmpty() else story.nickname.orEmpty()
    snippet = story.nickname.orEmpty()
    this.store = story
    this.posArray = posArray
  }
}
