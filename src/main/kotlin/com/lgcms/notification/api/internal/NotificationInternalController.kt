package com.lgcms.notification.api.internal

import com.lgcms.notification.service.NotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/student/notification")
class NotificationInternalController(
    private val notificationService: NotificationService,
) {
    @GetMapping("/status")
    fun getServerStatus(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(notificationService.getServerStatus())
    }
}