package grand.app.moon.helpers.map.cluster

import android.widget.Checkable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import grand.app.moon.domain.home.models.Store

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
    position = LatLng(story.latitude, story.longitude)
    title = if (!story.name.equals("")) story.name else story.nickname
    snippet = story.nickname
    this.store = story
    this.posArray = posArray
  }
}
