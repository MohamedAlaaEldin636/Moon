package com.structure.base_mvvm.domain.utils

data class BaseResponse<T>(
  val data: T,
  val msg: String,
  val status: Int,
)