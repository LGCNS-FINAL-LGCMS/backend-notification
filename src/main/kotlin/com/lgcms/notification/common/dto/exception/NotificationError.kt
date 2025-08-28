package com.lgcms.notification.common.dto.exception

import org.springframework.http.HttpStatus

enum class NotificationError(
    val status: String,
    val message: String,
    val httpStatus: HttpStatus
) : ErrorCodeInterface {
    NO_SUCH_NOTIFICATION_TYPE("NOTI_001", "존재하지 않는 알림 타입입니다.", HttpStatus.NOT_FOUND),
    ;

    override fun getErrorCode(): ErrorCode {
        return ErrorCode(
            status = status,
            message = message,
            httpStatus = httpStatus
        )
    }
}
