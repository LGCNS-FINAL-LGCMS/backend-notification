package com.lgcms.notification.common.dto.exception

class BaseException(
    val errorCodeInterface: ErrorCodeInterface
) : RuntimeException() {
    fun getErrorCode(): ErrorCode {
        return errorCodeInterface.getErrorCode()
    }
}
