package com.lgcms.notification.common.dto

data class BaseResponse<T>(
    val status: String,
    val message: String,
    val data: T?
) {
    companion object {
        fun <T> ok(result: T): BaseResponse<T> {
            return BaseResponse("OK", "호출에 성공했습니다", result)
        }

        fun <T> onFailure(status: String, message: String, data: T?): BaseResponse<T?> {
            return BaseResponse(status, message, data)
        }
    }
}