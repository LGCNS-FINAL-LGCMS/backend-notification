package com.lgcms.notification.advice

import com.lgcms.notification.common.dto.BaseResponse
import com.lgcms.notification.common.dto.exception.BaseException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class NotificationControllerAdvice {
    val log = LoggerFactory.getLogger(NotificationControllerAdvice::class.java)

    @ExceptionHandler(BaseException::class)
    fun handleException(e: BaseException): ResponseEntity<BaseResponse<String?>> {
        val errorCode = e.getErrorCode()
        return ResponseEntity
            .status(errorCode.httpStatus.value())
            .body(BaseResponse.onFailure(errorCode.status, errorCode.message, null))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(e: MethodArgumentNotValidException): ResponseEntity<BaseResponse<String?>> {
        val errorMessage = e.bindingResult
            .fieldErrors
            .firstOrNull()
            ?.defaultMessage
            ?: "잘못된 요청입니다."
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST.value())
            .body(BaseResponse.onFailure("ARGUMENT_ERROR", errorMessage, null))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<BaseResponse<String?>> {
        log.error("에러 발생 : ", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(BaseResponse.onFailure("INTERNAL_SERVER_ERRROR", "내부 서버 에러", null))
    }
}