package grand.app.moon.data.remote

import android.util.Log
import com.google.gson.Gson
import grand.app.moon.helpers.paging.MABaseResponse
import grand.app.moon.core.extenstions.loginPage
import grand.app.moon.core.extenstions.logout
import grand.app.moon.core.MyApplication
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.ErrorResponse
import grand.app.moon.domain.utils.FailureStatus
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.MyLogger
import grand.app.moon.helpers.paging.MAResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import java.util.HashMap
import javax.inject.Inject




open class BaseRemoteDataSource @Inject constructor() {
  var gson: Gson = Gson()
  private val TAG = "BaseRemoteDataSource"
  fun getParameters(requestData: Any): Map<String, String> {
    val params: MutableMap<String, String> = HashMap()
    try {
      val jsonObject = JSONObject(gson.toJson(requestData))
      Log.d(TAG, "getParameters: $jsonObject")

      for (i in 0 until jsonObject.names().length()) {
        if (jsonObject[jsonObject.names().getString(i)] is JSONArray) {
//          Log.d(TAG, "getParameters: YES")
//          val propertyName = jsonObject.names()[i]
//          Log.d(TAG, "getParameters: $propertyName")
////          val name = params[jsonObject.names()[i]]
////          Log.d(TAG, "getParameters NAME: ${jsonObject.names()[i]}")
////          Log.d(TAG, "getParameters NAME: ${jsonObject.names().getString(i)}")
//          val list = jsonObject[jsonObject.names().getString(i)] as JSONArray
//          for (j in 0 until list.length()) {
//            val resultChild: MutableMap<String, String> = HashMap()
////            if(list[j] is JSONObject){
////              val child = JSONObject(gson.toJson(list[j]))
////              for(k in 0 until child.names().length()){
////                Log.d(TAG, "getParameters: ${child.names()[k]}")
////                resultChild[child.names().getString(k)] = child[child.names().getString(k).toString()].toString()
////              }
////            }
////            Log.d(TAG, "getParameters CHILD: ${resultChild}")
////            params[propertyName.toString()] = "[$resultChild]"
//          }
//          Log.d(TAG, "getParameters: size ${jsonArr.length()}")
//          for (i in 0 until jsonArr.length()) {
//            val j: JSONObject = jsonArr.optJSONObject(i)
//            Log.d(TAG, "getParameters: ${j}")
//          }
//          val jsonArray = jsonObject[jsonObject.names().getString(i)] as JSONArray
//          for (j in 0 until jsonArray.length()) {
//            val name = "${jsonObject.names().getString(i)}[$j]"
//            if(jsonArray[j] is JSONObject){
//              val jsonObjectParameter = JSONObject(gson.toJson(jsonArray[j]))
//
//              val temp: Iterator<String> = jsonObjectParameter.keys()
//              while (temp.hasNext()) {
//                val key = temp.next()
//                Log.d(TAG, "getParameters KEYS: $key")
//                val value: Any = jsonObjectParameter.get(key)
////                Log.d(TAG, "getParameters: $key with value $value")
//                if(jsonObjectParameter.get(key) is JSONObject){
//                  val jsonObjectFather = JSONObject(jsonObjectParameter.get(key).toString())
////                  Log.d(TAG, "getParameters: YES OBJECTTTTTTTTTTTTTTTTTTTT ${jsonObjectFather.names()}")
//                }
//              }
//              Log.d(TAG, "getParameters: ==============================")
////              Log.d(TAG, "getParameters: JSON OBJECT $jsonObjectParameter")
//              for (k in 0 until jsonObjectParameter.names().length()) {
////                Log.d(TAG, "getParameters: JSON OBJECT =>>>>>>>>>>> ${jsonObjectParameter.names()[0]} , with length ${jsonObjectParameter.names().length()}")
////                Log.d(TAG, "getParameters: JSON OBJECT =>||>>>>>>>> $k")
//                if(jsonObjectParameter.names()[0] is JSONObject)
////                  Log.d(TAG, "getParameters: JSON OBJECT =>>>>>>>>>>> YEP JSON OBJECT")
////                Log.d(TAG, "getParameters: JSON OBJECT =>>>>>>>>>>> ${jsonObjectParameter.names().getString(1)}")
////                Log.d(TAG, "getParameters: JSON OBJECT =>>>>>>>>>>> ${jsonObjectParameter.names().getString(2)}")
//                params[jsonObjectParameter.names().getString(k)] =
//                  jsonObject[jsonObjectParameter.names().getString(k)].toString()
//              }
//            }else
//              params[name] = jsonArray[j].toString()
//          }
        } else {
          if(jsonObject[jsonObject.names().getString(i)] != null && jsonObject.names().getString(i) != null) {
            params[jsonObject.names().getString(i)] =
              jsonObject[jsonObject.names().getString(i)].toString()
          }
        }
      }
    } catch (e: Exception) {
      Log.d(TAG, "getParameters: EXCE")
      e.stackTrace
    }
    Log.d(TAG, "getParameters: ${params}")
    return params
  }

	private var counter = 0
  suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
	  MyLogger.e("safeApiCall safeApiCall -> ch ${counter++} -> start")
    println(apiCall)
	  MyLogger.e("safeApiCall safeApiCall -> ch ${counter++} -> $apiCall")
    try {
      val apiResponse = apiCall.invoke()
	    MyLogger.e("safeApiCall safeApiCall -> ch ${counter++} -> $apiResponse")
      val gson = Gson()
      val json = gson.toJson(apiResponse)
	    MyLogger.e("safeApiCall safeApiCall -> ch ${counter++} -> $json")
      Log.d(TAG, "safeApiCall: $json")

//      println(json)
	    MyLogger.e("safeApiCall safeApiCall -> ch ${counter++} -> ${(apiResponse as? BaseResponse<*>)?.code}")
      when ((apiResponse as BaseResponse<*>).code) {
        403 -> {
          return Resource.Failure(FailureStatus.TOKEN_EXPIRED, (apiResponse as BaseResponse<*>).code, (apiResponse as BaseResponse<*>).message)
        }
        200 -> {
          return Resource.Success(apiResponse)
        }
        401 -> {
          return Resource.Failure(
            FailureStatus.EMPTY,
            (apiResponse as BaseResponse<*>).code,
            (apiResponse as BaseResponse<*>).message
          )
        }
        405 -> {
          return Resource.Failure(
            FailureStatus.NOT_ACTIVE,
            (apiResponse as BaseResponse<*>).code,
            (apiResponse as BaseResponse<*>).message
          )
        }
        else -> {
          return Resource.Failure(FailureStatus.API_FAIL)
        }
      }
    } catch (throwable: Throwable) {
	    MyLogger.e("safeApiCall safeApiCall -> ch ${counter++} -> throwable $throwable")
	    println(throwable)
      when(throwable){
        is HttpException -> {
          Log.d(TAG, "http_code: ${throwable.code()}")
	        MyLogger.e("safeApiCall safeApiCall -> ch ${counter++} -> throwable CODE ${throwable.code()}")
          when(throwable.code()){
            401 -> {
              val errorResponse = Gson().fromJson(
                throwable.response()?.errorBody()!!.charStream().readText(),
                ErrorResponse::class.java
              )

              MyApplication.instance.logout()
              MyApplication.instance.loginPage()
//              return Resource.Failure(
//                FailureStatus.TOKEN_EXPIRED,
//                throwable.code(),
//                errorResponse.detail
//              )
            }
          }
        }
      }
      return Resource.Failure(FailureStatus.API_FAIL, 404, "please try again")
//      when (throwable) {
//        is HttpException -> {
//          when {
//            throwable.code() == 422 -> {
//              val jObjError = JSONObject(throwable.response()!!.errorBody()!!.string())
//              val apiResponse = jObjError.toString()
//              val response = Gson().fromJson(apiResponse, BaseResponse::class.java)
//
//              return Resource.Failure(FailureStatus.API_FAIL, throwable.code(), response.message)
//            }
//            throwable.code() == 401 -> {
//              val errorResponse = Gson().fromJson(
//                throwable.response()?.errorBody()!!.charStream().readText(),
//                ErrorResponse::class.java
//              )
//              return Resource.Failure(
//                FailureStatus.TOKEN_EXPIRED,
//                throwable.code(),
//                errorResponse.detail
//              )
//            }
//            throwable.code() == 404 -> {
//              val errorResponse = Gson().fromJson(
//                throwable.response()?.errorBody()!!.charStream().readText(),
//                ErrorResponse::class.java
//              )
//
//              return Resource.Failure(
//                FailureStatus.API_FAIL,
//                throwable.code(),
//                errorResponse.detail
//              )
//            }
//            throwable.code() == 500 -> {
//              val errorResponse = Gson().fromJson(
//                throwable.response()?.errorBody()!!.charStream().readText(),
//                ErrorResponse::class.java
//              )
//
//              return Resource.Failure(
//                FailureStatus.API_FAIL,
//                throwable.code(),
//                errorResponse.detail
//              )
//            }
//            else -> {
//              return if (throwable.response()?.errorBody()!!.charStream().readText().isEmpty()) {
//                Resource.Failure(FailureStatus.API_FAIL, throwable.code())
//              } else {
//                try {
//                  val errorResponse = Gson().fromJson(
//                    throwable.response()?.errorBody()!!.charStream().readText(),
//                    ErrorResponse::class.java
//                  )
//                  Resource.Failure(FailureStatus.API_FAIL, throwable.code(), errorResponse?.detail)
//                } catch (ex: JsonSyntaxException) {
//                  Resource.Failure(FailureStatus.API_FAIL, throwable.code())
//                }
//              }
//            }
//          }
//        }
//
//        is UnknownHostException -> {
//          return Resource.Failure(FailureStatus.NO_INTERNET)
//        }
//
//        is ConnectException -> {
//          return Resource.Failure(FailureStatus.NO_INTERNET)
//        }
//
//        else -> {
//          return Resource.Failure(FailureStatus.OTHER)
//        }
//      }
    }
  }

  suspend fun <T> safeApiCall2(
    apiCall: suspend () -> MABaseResponse<T>
  ): MAResult.Immediate<MABaseResponse<T>> = withContext(Dispatchers.IO) {
    try {

			MyLogger.e("jsadkjash ch 1")

      val response = apiCall()

	    MyLogger.e("jsadkjash ch 2 $response")

	    val errorStatus = when (response.code) {
        200 -> {
          return@withContext MAResult.Success(response)
        }
        403 -> {
          MAResult.Failure.Status.TOKEN_EXPIRED
        }
        /*405 -> {
            // Not used in this project as always on login you should re-verify, and in
            // case of social login check if has phone number if so then just ignore
            // verification step
            MAResult.Failure.Status.ACTIVATION_NOT_VERIFIED
        }*/
        401 -> {
          MAResult.Failure.Status.ERROR
        }
        in 500 until 600 -> {
          MAResult.Failure.Status.SERVER_ERROR
        }
        else -> {
          MAResult.Failure.Status.OTHER
        }
      }

      MAResult.Failure(errorStatus, response.code, response.message)
    }catch (throwable: Throwable) {
      Log.d(TAG, "iduaosiudaso eeeeeeeeeeeeeee $throwable")

      when (throwable) {
        is HttpException -> {

          val errorStatus = when (throwable.code()) {
            in 400 until 500 -> MAResult.Failure.Status.ERROR
            in 500 until 600 -> MAResult.Failure.Status.SERVER_ERROR
            else -> MAResult.Failure.Status.OTHER
          }

          MAResult.Failure(errorStatus, throwable.code(), throwable.message())
        }
        is UnknownHostException, is SocketException, is ConnectException -> {
        MAResult.Failure(MAResult.Failure.Status.NO_INTERNET, message = throwable.message)
      }
        else -> {
          MAResult.Failure(MAResult.Failure.Status.OTHER, message = throwable.message)
        }
      }
    }
  }
}