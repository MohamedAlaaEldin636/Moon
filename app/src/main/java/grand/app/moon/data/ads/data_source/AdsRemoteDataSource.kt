package grand.app.moon.data.ads.data_source

import android.util.Log
import grand.app.moon.BuildConfig
import grand.app.moon.data.remote.BaseRemoteDataSource
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.auth.entity.request.*
import grand.app.moon.domain.filter.entitiy.FilterResultRequest
import grand.app.moon.domain.home.models.InteractionRequest
import grand.app.moon.domain.home.models.Property
import grand.app.moon.domain.home.models.review.ReviewRequest
import grand.app.moon.extensions.MyLogger
import grand.app.moon.presentation.filter.FILTER_TYPE
import grand.app.moon.presentation.myAds.model.TypeOfAd
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap
import javax.inject.Inject

class AdsRemoteDataSource @Inject constructor(private val apiService: AdsServices) :
  BaseRemoteDataSource() {

  private val TAG = "AdsRemoteDataSource"
  suspend fun getDetails(id: Int, type: Int) = safeApiCall {
    apiService.details(id, type)
  }

  suspend fun favourite(addFavouriteAdsRequest: AddFavouriteAdsRequest) = safeApiCall {
    apiService.favourite(addFavouriteAdsRequest)
  }

  suspend fun getProfileAdsList(page: Int, type: Int?) = safeApiCall {
    Log.d(TAG, "getProfileAdsList: $page")
    Log.d(TAG, "getProfileAdsList: $type")
    apiService.getProfileAdsList(page, type)
  }
  /*
      @Query("type") type: Int?,
    @Query("category_id") categoryId: Int?,
    @Query("sub_category_id") subCategoryId: Int?,
    @Query("order_by") orderBy: Int? = 1,
    @Query("store_id") storeId: Int?,
    @Query("page") page: Int?,
   */

  suspend fun getAdsList(
    type: Int?,
    categoryId: Int?,
    subCategoryId: Int?,
    orderBy: Int?,
    storeId: Int?,
    other_options:Int,
    search: String = "",
    page: Int?
  ) = safeApiCall {
    apiService.getAds(type, categoryId, subCategoryId, orderBy, storeId,other_options, search, page)
  }


  suspend fun getAdsSubCategory(
    type: Int?,
    categoryId: Int?,
    subCategoryId: Int?,
    orderBy: Int?,
    storeId: Int?,
    search: String = "",
    properties: ArrayList<Property>?,
    page: Int?
  ) = safeApiCall {
    var propery: Int? = null
    properties?.let {
      if (it.isNotEmpty()) propery = it[0].id
    }
    Log.d(TAG, "getAdsSubCategory: $propery")
    apiService.getAdsSubCategory(
      type,
      categoryId,
      subCategoryId,
      orderBy,
      storeId,
      search,
      propery,
      page
    )
  }


  suspend fun getReviews(page: Int, storeId: String?, advertisementId: String?) = safeApiCall {
    apiService.getReviews(storeId, advertisementId, page)
  }

  suspend fun addReview(request: ReviewRequest) = safeApiCall {
    apiService.addReview(getParameters(request))
  }

  suspend fun setInteraction(request: InteractionRequest) = safeApiCall {
    apiService.setInteraction(request)
  }

  suspend fun filterDetails(categoryId: Int, subCategoryId: Int?) = safeApiCall {
    var url = "${BuildConfig.API_BASE_URL}v1/filter/details?category_id=$categoryId"
    if (subCategoryId != null && subCategoryId != -1) url += "&sub_category_id=$subCategoryId"
    apiService.filterDetails(url)
  }

  suspend fun search(search: String?,page:Int) = safeApiCall {
    apiService.search(search,page)
  }

  suspend fun getFilterDetails2(categoryId: Int, subCategoryId: Int) = safeApiCall {
    apiService.getFilterDetails2(categoryId.toString(), subCategoryId.toString())
  }

	suspend fun addAdvertisement(
		category_id: Int,
		sub_category_id: Int,
		images: List<MultipartBody.Part>,
		title: String,
		latitude: String,
		longitude: String,
		address: String,
		city_id: Int?,
		price: Int,
		is_negotiable: Int,
		brand_id: Int?,
		description: String,
		propertiesIds: List<Pair<Int, String?>>,
		price_before: Int?,
		store_category_id: Int?,
		store_sub_category_id: Int?,
	) = safeApiCall {
		val map = mutableMapOf<String, RequestBody>()
		for ((index, pair) in propertiesIds.withIndex()) {
			val (id, text) = pair
			map["property_ids[$index][id]"] = id.toString().toRequestBody()
			if (text.isNullOrEmpty().not()) {
				map["property_ids[$index][text]"] = text.orEmpty().toRequestBody()
			}
		}

		if (brand_id != null) {
			map["brand_id"] = brand_id.toString().toRequestBody()
		}

		if (description.isNotEmpty()) {
			map["description"] = description.toRequestBody()
		}

		map["title"] = title.toRequestBody()

		if (city_id != null) {
			map["city_id"] = city_id.toString().toRequestBody()
		}
		if (price_before != null) {
			map["price_before"] = price_before.toString().toRequestBody()
		}
		if (store_category_id != null) {
			map["store_category_id"] = store_category_id.toString().toRequestBody()
		}
		if (store_sub_category_id != null) {
			map["store_sub_category_id"] = store_sub_category_id.toString().toRequestBody()
		}

		apiService.addAdvertisement(
	    category_id,
	    sub_category_id,
	    images,
	    latitude.toRequestBody(),
	    longitude.toRequestBody(),
			address.toRequestBody(),
	    price,
	    is_negotiable,
	    map,
    )
  }

	suspend fun updateAdvertisementToBePremium(advId: Int) = safeApiCall {
		apiService.updateAdvertisementToBePremium(advId)
	}

	suspend fun updateAdvertisement(
		advId: Int,
		category_id: Int,
		sub_category_id: Int,
		images: List<MultipartBody.Part>,
		title: String,
		latitude: String,
		longitude: String,
		address: String,
		city_id: Int?,
		price: Int,
		is_negotiable: Int,
		brand_id: Int?,
		description: String,
		propertiesIds: List<Pair<Int, String?>>,
		price_before: Int?,
		store_category_id: Int?,
		store_sub_category_id: Int?,
	) = safeApiCall {
		val map = mutableMapOf<String, RequestBody>()
		for ((index, pair) in propertiesIds.withIndex()) {
			val (id, text) = pair
			map["property_ids[$index][id]"] = id.toString().toRequestBody()
			if (text.isNullOrEmpty().not()) {
				map["property_ids[$index][text]"] = text.orEmpty().toRequestBody()
			}
		}

		if (brand_id != null) {
			map["brand_id"] = brand_id.toString().toRequestBody()
		}

		if (description.isNotEmpty()) {
			map["description"] = description.toRequestBody()
		}

		map["title"] = title.toRequestBody()

		if (city_id != null) {
			map["city_id"] = city_id.toString().toRequestBody()
		}
		if (price_before != null) {
			map["price_before"] = price_before.toString().toRequestBody()
		}
		if (store_category_id != null) {
			map["store_category_id"] = store_category_id.toString().toRequestBody()
		}
		if (store_sub_category_id != null) {
			map["store_sub_category_id"] = store_sub_category_id.toString().toRequestBody()
		}

		apiService.updateAdvertisement(
	    advId,
			category_id,
			sub_category_id,
	    images,
	    latitude.toRequestBody(),
	    longitude.toRequestBody(),
			address.toRequestBody(),
	    price,
	    is_negotiable,
	    map,
    )
  }

	suspend fun getMyAdvertisementDetails(id: Int) = safeApiCall { apiService.getMyAdvertisementDetails(id) }

	suspend fun deleteAdvertisement(id: Int) = safeApiCall { apiService.deleteAdvertisement(id) }
	suspend fun deleteImageInAdvertisement(id: Int) = safeApiCall { apiService.deleteImageInAdvertisement(id) }

	suspend fun makeMyAdvertisementPremium(id: Int, packageId: Int) = safeApiCall { apiService.makeMyAdvertisementPremium(id, packageId) }

	suspend fun getMyAdvertisements(
		title: String?,
		typeOfAd: TypeOfAd?,
		fromDate: String?,
		toDate: String?,
	) = safeApiCall {
		val map = mutableMapOf<String, String>()

		if (!title.isNullOrEmpty()) {
			map["title"] = title
		}

		if (!fromDate.isNullOrEmpty()) {
			map["from"] = fromDate
		}

		if (!toDate.isNullOrEmpty()) {
			map["to"] = toDate
		}

		val premium = typeOfAd?.apiValue
		if (premium != null) {
			map["premium"] = premium.toString()
		}

		apiService.getMyAdvertisements(map)
	}

  suspend fun filterResults(request: FilterResultRequest) = safeApiCall {
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
    Log.d(TAG, "filterResults: ===========================")
    Log.d(TAG, "filterResults: $map")
    apiService.filterResults(map)
  }


}