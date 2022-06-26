package grand.app.moon.data.store.data_source

import android.util.Log
import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.store.entity.*
import grand.app.moon.presentation.filter.FILTER_TYPE
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(private val apiService: StoreServices) :
  BaseRemoteDataSource() {

  suspend fun follow(storeRequest: FollowStoreRequest) = safeApiCall {
    apiService.follow(storeRequest)
  }

  suspend fun unBlock(storeRequest: FollowStoreRequest) = safeApiCall {
    apiService.unBlock(storeRequest)
  }

  suspend fun unBlock(storeRequest: ArrayList<Int>) = safeApiCall {
    apiService.unBlock(storeRequest)
  }


  suspend fun storeDetails(id: Int, type: Int) = safeApiCall {
    apiService.storeDetails(id, type)
  }

  private val TAG = "StoreRemoteDataSource"
  suspend fun getStores(request: StoreFilterRequest) = safeApiCall {
    val map = getParameters(request).toMutableMap()
//    if(map.containsKey("properties"))
//      Log.d(TAG, "filterResults: HAVE")
//    else
//      Log.d(TAG, "filterResults: NOT HAVE")

    var counter = 0
    Log.d(TAG, "filterResults: $map")
    for ((index, item) in request.properties.orEmpty().withIndex()) {
      when {
        item.filterType == FILTER_TYPE.CITY -> {
          request.cityIds?.forEachIndexed { index, city ->
            map["city_ids[$index]"] = city.toString()
          }
        }
        item.filterType == FILTER_TYPE.AREA -> {
          request.areaIds?.forEachIndexed { index, area ->
            map["area_ids[$index]"] = area.toString()
          }
        }
        item.filterType != FILTER_TYPE.CATEGORY && item.filterType != FILTER_TYPE.SUB_CATEGORY
          && item.filterType != FILTER_TYPE.CITY -> {
          if (item.selectedList.isNotEmpty()) {
            item.selectedList.forEachIndexed { indexProp, propSelect ->
              map["properties[$counter][id]"] = propSelect.toString()
              counter++
            }
          } else {
            if (item.from != null && item.from!!.isNotEmpty() && item.to != null && item.to!!.isNotEmpty() && item.selectedList.isEmpty()) {
              map["properties[$counter][id]"] = item.id.toString()
              item.from?.let {
                map["properties[$counter][from]"] = it
              }
              item.to?.let {
                map["properties[$counter][to]"] = it
              }
              counter++
            }
          }
        }
      }
    }
    apiService.getStores(map)
  }

  suspend fun storyAction(request: StoryRequest) = safeApiCall {
    Log.d(TAG, "getStores: ")
    apiService.storyAction(request)
  }


  suspend fun getFavouriteStores(page: Int) = safeApiCall {
    apiService.getFavouriteStores(page)
  }

  suspend fun getBlockedStores(page: Int) = safeApiCall {
    apiService.getBlockedStores(page)
  }

  suspend fun getWhatsappStores(page: Int) = safeApiCall {
    apiService.getWhatsappStores(page)
  }


  suspend fun report(page: ReportStoreRequest) = safeApiCall {
    apiService.report(page)
  }

  suspend fun reportAds(page: ReportAdsRequest) = safeApiCall {
    apiService.reportAds(page)
  }


  suspend fun share(page: ShareRequest) = safeApiCall {
    apiService.share(page)
  }

  suspend fun getUsersViewFollowing(storeId: Int, type: String,page: Int) = safeApiCall {
    apiService.getUsersViewFollowing(storeId, type,page)
  }


}