package grand.app.moon.appMoonHelper

import android.util.Log
import grand.app.moon.domain.home.models.Advertisement
import kotlin.math.log

object ListHelper {
  val adsList = mutableMapOf<Int,Boolean>()
  val blockStores = mutableListOf<Int>()

  fun addAllAds(list : ArrayList<Advertisement>){
    list.forEach {
      adsList[it.id] = it.isFavorite
    }
  }

  fun addOrUpdate(adsId: Int , value: Boolean){
    adsList[adsId] = value
  }

  fun isExist(adsId : Int) : Boolean{
    return adsList.containsKey(adsId)
  }

  fun getFavourite(adsId : Int) : Boolean{
    if(adsList.containsKey(adsId))
      return adsList[adsId]!!
    return false
  }

  fun addToBlock(storeId: Int){
    if(!checkBlockStore(storeId)) blockStores.add(storeId)
  }

  fun removeFromBlock(storeId: Int){
    blockStores.forEachIndexed { index , store ->
      blockStores.add(storeId)
    }
  }
   val TAG = "ListHelper"

  fun checkBlockStore(id: Int) : Boolean {
    blockStores.forEach {
      Log.d(TAG, "checkBlockStore: $it")
    }
    Log.d(TAG, "checkBlockStore: $id")
    return blockStores.contains(id)
  }

  fun removeStoreBlock(storeId: Int) {
    val pos = blockStores.indexOf(storeId)
    if(pos != -1) blockStores.removeAt(pos)
  }
}