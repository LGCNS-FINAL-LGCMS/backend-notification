package com.lgcms.notification.common.kafka.util.serializer

import com.lgcms.notification.common.kafka.util.KafkaObjectMapper.create
import org.apache.kafka.common.serialization.Deserializer

class JsonKafkaDeserializer<T>(private val targetType: Class<T>) : Deserializer<T> {
    private val objectMapper = create()

    override fun deserialize(topic: String?, data: ByteArray?): T? {
        return try {
            data?.let { objectMapper.readValue(it, targetType) }
        } catch (e: Exception) {
            throw RuntimeException("Kafka JSON Deserialize error", e)
        }
    }
}