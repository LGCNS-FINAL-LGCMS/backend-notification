package com.lgcms.notification.common.kafka.config

import com.lgcms.notification.common.kafka.util.serializer.JsonKafkaDeserializer
import com.lgcms.notification.common.kafka.util.serializer.JsonKafkaSerializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
@EnableKafka
class KafkaConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String,
    @Value("\${spring.kafka.consumer.group-id}")
    private val groupId: String,
) {

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val config: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonKafkaSerializer::class.java)
        return DefaultKafkaProducerFactory(config)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory())
    }

    private fun commonConsumerProps(): MutableMap<String?, Any?> {
        val props: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer::class.java)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer::class.java)
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer::class.java)
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer::class.java)
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*")
        return props
    }

    fun <T> consumerFactory(valueType: Class<T>): ConsumerFactory<String, T> {
        val props = commonConsumerProps().toMutableMap()
        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            JsonKafkaDeserializer(valueType)
        )
    }

    fun <T> kafkaListenerContainerFactory(valueType: Class<T>): ConcurrentKafkaListenerContainerFactory<String, T> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, T>()
        factory.consumerFactory = consumerFactory(valueType)
        return factory
    }
}