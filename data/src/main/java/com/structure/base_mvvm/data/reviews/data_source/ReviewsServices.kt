package com.structure.base_mvvm.data.reviews.data_source

import com.structure.base_mvvm.domain.reviews.entity.ReviewRequest
import com.structure.base_mvvm.domain.reviews.entity.Reviews
import com.structure.base_mvvm.domain.reviews.entity.ReviewsMain
import com.structure.base_mvvm.domain.utils.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewsServices {
  @GET("v1/student/instructors/{instructor_id}/reviews?page={page}")
  suspend fun reviews(
    @Path("instructor_id") instructor_id: Int,
    @Path("page") page: Int
  ): BaseResponse<ReviewsMain>

  @POST("v1/student/instructors/{instructor_id}/review")
  suspend fun sendReview(
    @Path("instructor_id") instructor_id: Int,
    @Body request: ReviewRequest
  ): BaseResponse<*>

}