package grand.app.moon.appMoonHelper

import grand.app.moon.domain.home.models.Advertisement

object ListHelper {
  val adsList = mutableMapOf<Int,Boolean>()

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
}