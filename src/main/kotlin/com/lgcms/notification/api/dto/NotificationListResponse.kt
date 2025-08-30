package com.lgcms.notification.api.dto

import com.lgcms.notification.domain.NotificationEntity

data class NotificationListResponse(
    val notifications: List<NotificationEntity>
)
