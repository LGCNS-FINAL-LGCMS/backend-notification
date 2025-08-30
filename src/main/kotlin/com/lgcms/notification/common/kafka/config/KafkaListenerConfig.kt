package com.lgcms.notification.common.kafka.config

import com.lgcms.notification.common.kafka.dto.KafkaEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory

@Configuration
class KafkaListenerConfig(private val kafkaConfig: KafkaConfig) {
    @Bean
    fun defaultFactory(): ConcurrentKafkaListenerContainerFactory<String, KafkaEvent<*>> =
        kafkaConfig.kafkaListenerContainerFactory(KafkaEvent::class.java)
}