package com.lgcms.notification.common.dto.exception

import org.springframework.http.HttpStatus

data class ErrorCode(
    val status: String,
    val message: String,
    val httpStatus: HttpStatus,
)