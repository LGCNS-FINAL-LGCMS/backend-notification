package com.lgcms.notification.event.kafka.consumer

import com.lgcms.notification.common.kafka.dto.KafkaEvent
import com.lgcms.notification.event.redis.publisher.NotificationPublisher
import com.lgcms.notification.service.NotificationService
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class NotificationConsumer(
    private val notificationService: NotificationService,
    private val notificationPublisher: NotificationPublisher,
) {
    private val logger = LoggerFactory.getLogger(NotificationConsumer::class.java)

    @KafkaListener(
        topics = ["NOTIFICATION"],
        containerFactory = "defaultFactory",
    )
    suspend fun handleNotificationEvent(
        @Payload kafkaEvent: KafkaEvent<*>,
    ) {
        try {
            val notificationEntity = notificationService.convertEventAndSave(kafkaEvent)

            runBlocking {
                notificationPublisher.publishNotification(notificationEntity)
            }
        } catch (e: Exception) {
            logger.error(
                "알림 처리 중 오류 발생: eventId={}, error={}",
                kafkaEvent.eventId, e.message, e
            )
        }
    }
}
