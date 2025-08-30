package com.lgcms.notification.common.kafka.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.lgcms.notification.common.kafka.dto.KafkaEvent
import org.springframework.stereotype.Component

@Component
class KafkaEventFactory(
    private val objectMapper: ObjectMapper
) {
    fun <T> convert(event: KafkaEvent<*>, clazz: Class<T>): T {
        return objectMapper.convertValue(event.data, clazz)
    }
}
