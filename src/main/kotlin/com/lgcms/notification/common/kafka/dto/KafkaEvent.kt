package com.lgcms.notification.common.kafka.dto

data class KafkaEvent<T>(
    val eventId: String,
    val eventType: String,
    val eventTime: String,
    val data: T,
)
