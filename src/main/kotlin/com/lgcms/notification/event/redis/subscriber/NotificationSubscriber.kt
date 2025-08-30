package com.lgcms.notification.event.redis.subscriber

import com.fasterxml.jackson.databind.ObjectMapper
import com.lgcms.notification.domain.NotificationEntity
import com.lgcms.notification.service.NotificationSSEBroadcaster
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component

@Component
class NotificationSubscriber(
    private val redisMessageListenerContainer: RedisMessageListenerContainer,
    private val sseBroadcaster: NotificationSSEBroadcaster,
    private val objectMapper: ObjectMapper,
) : MessageListener {

    private val ChannelName = "notifications_internal"

    @PostConstruct
    fun subscribeToNotifications() {
        redisMessageListenerContainer.addMessageListener(this, ChannelTopic(ChannelName))
    }

    override fun onMessage(message: Message, pattern: ByteArray?) {
        handleNotification(String(message.body))
    }

    private fun handleNotification(notification: String) {
        val notificationData = objectMapper.readValue(notification, NotificationEntity::class.java)
        sseBroadcaster.broadcast(notificationData.memberId, notificationData)
    }
}
