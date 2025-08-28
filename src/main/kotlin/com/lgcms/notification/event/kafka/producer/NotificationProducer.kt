package com.lgcms.notification.event.kafka.producer

import com.lgcms.notification.common.kafka.dto.KafkaEvent
import com.lgcms.notification.event.kafka.dto.NotificationEventRequest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class NotificationProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) {
    suspend fun sendNotificationEvent(event: KafkaEvent<NotificationEventRequest.QnaCreated>) {
        kafkaTemplate.send("NOTIFICATION", event.eventId, event)
    }
}
