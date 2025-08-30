package com.lgcms.notification.common.kafka.util.serializer

import com.lgcms.notification.common.kafka.util.KafkaObjectMapper.create
import org.apache.kafka.common.serialization.Serializer

class JsonKafkaSerializer<T> : Serializer<T?> {
    private val objectMapper = create()

    override fun serialize(topic: String?, data: T?): ByteArray? {
        try {
            return objectMapper.writeValueAsBytes(data)
        } catch (e: Exception) {
            throw RuntimeException("Kafka JSON Serialize error", e)
        }
    }
}