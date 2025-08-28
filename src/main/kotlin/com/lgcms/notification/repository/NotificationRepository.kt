package com.lgcms.notification.repository

import com.lgcms.notification.domain.NotificationEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface NotificationRepository : CoroutineCrudRepository<NotificationEntity, UUID> {
    @Query("""
        SELECT *
        FROM notifications AS n
        WHERE n.member_id = :memberId
        ORDER BY n.created_at DESC
        """)
    suspend fun findByMemberId(memberId: Long): List<NotificationEntity>
}