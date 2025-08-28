package com.lgcms.notification.event.redis.publisher

import com.lgcms.notification.domain.NotificationEntity
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class NotificationPublisher(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private val ChannelName = "notifications_internal"

    fun publishNotification(notification: NotificationEntity) {
        redisTemplate.convertAndSend(ChannelName, notification)
    }

    fun publishToChannel(channel: String, message: String) {
        redisTemplate.convertAndSend(channel, message)
    }
}
