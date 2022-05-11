package grand.app.moon.data.remote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonSyntaxException
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.ErrorResponse
import grand.app.moon.domain.utils.FailureStatus
import grand.app.moon.domain.utils.Resource
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
      for (i in 0 until jsonObject.names().length()) {
        if (jsonObject[jsonObject.names().getString(i)] is JSONArray) {
          val jsonArray = jsonObject[jsonObject.names().getString(i)] as JSONArray
          for (j in 0 until jsonArray.length()) {
            val name = "${jsonObject.names().getString(i)}[$j]"
            if(jsonArray[j] is JSONObject){
              val jsonObjectParameter = JSONObject(gson.toJson(jsonArray[j]))
              for (k in 0 until jsonObjectParameter.names().length()) {
                params[jsonObjectParameter.names().getString(k)] =
                  jsonObject[jsonObjectParameter.names().getString(k)].toString()
              }
            }else
              params[name] = jsonArray[j].toString()
          }
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
    return params
  }

  suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    println(apiCall)
    try {
      val apiResponse = apiCall.invoke()
      println(apiResponse)
      when ((apiResponse as BaseResponse<*>).code) {
        403 -> {
          return Resource.Failure(FailureStatus.TOKEN_EXPIRED)
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
      println(throwable)
      when (throwable) {
        is HttpException -> {
          when {
            throwable.code() == 422 -> {
              val jObjError = JSONObject(throwable.response()!!.errorBody()!!.string())
              val apiResponse = jObjError.toString()
              val response = Gson().fromJson(apiResponse, BaseResponse::class.java)

              return Resource.Failure(FailureStatus.API_FAIL, throwable.code(), response.message)
            }
            throwable.code() == 401 -> {
              val errorResponse = Gson().fromJson(
                throwable.response()?.errorBody()!!.charStream().readText(),
                ErrorResponse::class.java
              )
              return Resource.Failure(
                FailureStatus.TOKEN_EXPIRED,
                throwable.code(),
                errorResponse.detail
              )
            }
            throwable.code() == 404 -> {
              val errorResponse = Gson().fromJson(
                throwable.response()?.errorBody()!!.charStream().readText(),
                ErrorResponse::class.java
              )

              return Resource.Failure(
                FailureStatus.API_FAIL,
                throwable.code(),
                errorResponse.detail
              )
            }
            throwable.code() == 500 -> {
              val errorResponse = Gson().fromJson(
                throwable.response()?.errorBody()!!.charStream().readText(),
                ErrorResponse::class.java
              )

              return Resource.Failure(
                FailureStatus.API_FAIL,
                throwable.code(),
                errorResponse.detail
              )
            }
            else -> {
              return if (throwable.response()?.errorBody()!!.charStream().readText().isEmpty()) {
                Resource.Failure(FailureStatus.API_FAIL, throwable.code())
              } else {
                try {
                  val errorResponse = Gson().fromJson(
                    throwable.response()?.errorBody()!!.charStream().readText(),
                    ErrorResponse::class.java
                  )
                  Resource.Failure(FailureStatus.API_FAIL, throwable.code(), errorResponse?.detail)
                } catch (ex: JsonSyntaxException) {
                  Resource.Failure(FailureStatus.API_FAIL, throwable.code())
                }
              }
            }
          }
        }

        is UnknownHostException -> {
          return Resource.Failure(FailureStatus.NO_INTERNET)
        }

        is ConnectException -> {
          return Resource.Failure(FailureStatus.NO_INTERNET)
        }

        else -> {
          return Resource.Failure(FailureStatus.OTHER)
        }
      }
    }
  }

  suspend fun <T> safeApiCall2(
    apiCall: suspend () -> MABaseResponse<T>
  ): MAResult.Immediate<MABaseResponse<T>> = withContext(Dispatchers.IO) {
    try {


      val response = apiCall()

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
      Log.d(TAG, "iduaosiudaso eeeeeeeeeeeeeee")

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