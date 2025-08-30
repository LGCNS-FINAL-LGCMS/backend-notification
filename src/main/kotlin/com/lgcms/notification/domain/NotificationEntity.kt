package com.lgcms.notification.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "notifications")
data class NotificationEntity(
    @Id
    @Column("id")
    val id: UUID? = null,
    @Column("notification_type")
    val notificationType: NotificationType,
    @Column("member_id")
    val memberId: Long,
    @Column("content")
    val content: String,
    @Column("web_path")
    val webPath: String,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)