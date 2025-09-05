package com.lgcms.notification.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.resource.ClientResources
import io.lettuce.core.tracing.MicrometerTracing
import io.micrometer.observation.ObservationRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}") private val redisHost: String,
    @Value("\${spring.data.redis.port}") private val redisPort: Int,
    @Value("\${spring.data.redis.database}") private val redisDatabase: Int,
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun redisConnectionFactory(clientResources: ClientResources): RedisConnectionFactory {
        val clientCOnfig = LettuceClientConfiguration.builder()
            .clientResources(clientResources)
            .build()
        val redisConfig = RedisStandaloneConfiguration(redisHost, redisPort)
        redisConfig.database = redisDatabase
        return LettuceConnectionFactory(redisConfig, clientCOnfig)
    }

    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()
        template.connectionFactory = connectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        return template
    }

    @Bean
    fun redisMessageListenerContainer(
        connectionFactory: RedisConnectionFactory
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(connectionFactory)
        return container
    }

    @Bean
    fun clientResources(observationRegistry: ObservationRegistry): ClientResources {
        return ClientResources.builder()
            .tracing(MicrometerTracing(observationRegistry, "redis-cache"))
            .build()
    }
}