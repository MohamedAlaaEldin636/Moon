package grand.app.moon.helpers.map.cluster

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import grand.app.moon.R
import grand.app.moon.helpers.map.MapConfig
import java.lang.Exception
import java.util.HashMap

class MarkerRender(mapConfig: MapConfig, items: List<ClusterCustomItem?>?) :
  DefaultClusterRenderer<ClusterCustomItem>(
    getApplicationContext(),
    mapConfig.getGoogleMap(),
    mapConfig.clusterManager
  ) {
  private val mIconGenerator: IconGenerator
  private val mClusterIconGenerator: IconGenerator
  private val mapConfig: MapConfig
  var bitmapStories = HashMap<String, Bitmap>()
  fun addMarker(clusterCustomItem: ClusterCustomItem, markerOptions: Marker?) {
    try {
      val markerView: View = (mapConfig.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
        R.layout.custom_marker_layout,
        null
      )
      if (mapConfig.isVisible) {
        drawStory(clusterCustomItem, markerOptions, markerView)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun drawStory(clusterCustomItem: ClusterCustomItem, markerOptions: Marker?, markerView: View) {
    try {
      markerView.findViewById<MaterialButton>(R.id.tv_marker_text).text = clusterCustomItem.getStore().nickname
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
  }

  override fun onBeforeClusterItemRendered(
    clusterCustomItem: ClusterCustomItem,
    markerOptions: MarkerOptions
  ) {
    try {
      markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_foreground))
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
  }

  //    private final IconGenerator mClusterIconGeneratorBig = new IconGenerator(getCtx());
  override fun onBeforeClusterRendered(
    cluster: Cluster<ClusterCustomItem>,
    markerOptions: MarkerOptions
  ) {
    super.onBeforeClusterRendered(cluster, markerOptions)
    //        Log.d(TAG,"nrte:"+clusterItem.getStory().storyView);
  }

  override fun onClusterItemRendered(clusterItem: ClusterCustomItem, marker: Marker) {
//        super.onClusterItemRendered(clusterItem, marker);
    try {
      marker.tag = clusterItem
      addMarker(clusterItem, marker)
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
  }

  companion object {
    private const val TAG = "MarkerRender"
    fun createImage(width: Int, height: Int, color: Int): Bitmap {
      val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
      val canvas = Canvas(bitmap)
      val paint = Paint()
      paint.color = color
      canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
      return bitmap
    }
  }

  init {
    this.mapConfig = mapConfig
    mIconGenerator = IconGenerator(mapConfig.context)
    mClusterIconGenerator = IconGenerator(mapConfig.context)
    //        mapConfig.clusterManager.addItems(items);


//        MarkerManager.Collection markerCollection = mapConfig.onClusterItemUpdatedclusterManager.getMarkerCollection();
//        Log.d(TAG, "=========>sadas asdasd");
//
//        Collection<Marker> markers = markerCollection.getMarkers();
//        for (Marker m : markers) {
//            Log.d(TAG, "lat:=>" + m.getPosition().latitude);
//            Log.d(TAG, "lng:=>" + m.getPosition().longitude);
////            Log.d(TAG, "<=" + clusterCustomItem.getStory().mLat);
////            if (clusterCustomItem.getStory().mLat == m.getPosition().latitude) {
////                m.setIcon(bitmapDescriptorFactory);
////            }
//        }
  }
}
