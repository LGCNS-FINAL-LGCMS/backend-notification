package com.lgcms.notification.api.open

import com.lgcms.notification.api.dto.SimpleStringResponse
import com.lgcms.notification.common.dto.BaseResponse
import com.lgcms.notification.common.kafka.dto.KafkaEvent
import com.lgcms.notification.event.kafka.dto.NotificationEventRequest
import com.lgcms.notification.service.NotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/notification")
class NotificationAdminController(
    private val notificationService: NotificationService,
) {
    @PostMapping
    suspend fun sendNotification(
        @RequestBody request: KafkaEvent<NotificationEventRequest.QnaCreated>,
    ): ResponseEntity<BaseResponse<SimpleStringResponse>> {
        notificationService.sendNotification(request)
        return ResponseEntity.ok()
            .body(BaseResponse.ok(SimpleStringResponse("알림이 전송되었습니다.")))
    }

    @GetMapping("/status")
    fun getServerStatus(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(notificationService.getServerStatus())
    }
}
