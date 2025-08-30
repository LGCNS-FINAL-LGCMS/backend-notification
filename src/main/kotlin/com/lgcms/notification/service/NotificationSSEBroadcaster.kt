package com.lgcms.notification.service

import com.lgcms.notification.domain.NotificationEntity
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class NotificationSSEBroadcaster {
    private val subscribers = ConcurrentHashMap<Long, Channel<NotificationEntity>>()

    suspend fun subscribe(memberId: Long): Flow<NotificationEntity> {
        val channel = Channel<NotificationEntity>(Channel.Factory.UNLIMITED)
        subscribers[memberId] = channel
        return channel.receiveAsFlow()
            .onCompletion {
                subscribers.remove(memberId)
                channel.close()
            }
    }

    fun broadcast(memberId: Long, notification: NotificationEntity) {
        subscribers[memberId]?.trySend(notification)
    }

    fun getActiveSubscriberCount(): Int = subscribers.size

    @PreDestroy
    fun cleanup() {
        subscribers.values.forEach { it.close() }
        subscribers.clear()
    }
}