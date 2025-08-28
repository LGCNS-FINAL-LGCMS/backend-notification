package com.lgcms.notification.api.open

import com.lgcms.notification.api.dto.NotificationListResponse
import com.lgcms.notification.common.dto.BaseResponse
import com.lgcms.notification.domain.NotificationEntity
import com.lgcms.notification.service.NotificationService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/student/notification")
class NotificationStudentController(
    private val notificationService: NotificationService,
) {
    @GetMapping(
        value = ["/subscribe"],
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    suspend fun getUserNotificationStream(
        @RequestHeader("X-USER-ID") memberId: Long,
    ): Flow<NotificationEntity> {
        return notificationService.subscribe(memberId)
    }

    @GetMapping()
    suspend fun getUserNotifications(
        @RequestHeader("X-USER-ID") memberId: Long,
    ): ResponseEntity<BaseResponse<NotificationListResponse>> {
        return ResponseEntity.ok()
            .body(BaseResponse.ok(NotificationListResponse(notificationService.findById(memberId))))
    }
}
