package com.lgcms.notification.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.lgcms.notification.api.dto.NotificationReadRequest
import com.lgcms.notification.common.dto.exception.BaseException
import com.lgcms.notification.common.dto.exception.NotificationError
import com.lgcms.notification.common.kafka.dto.KafkaEvent
import com.lgcms.notification.domain.NotificationEntity
import com.lgcms.notification.domain.NotificationEntityFactory
import com.lgcms.notification.domain.NotificationType
import com.lgcms.notification.event.kafka.dto.NotificationEventRequest
import com.lgcms.notification.event.kafka.producer.NotificationProducer
import com.lgcms.notification.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val notificationEntityFactory: NotificationEntityFactory,
    private val objectMapper: ObjectMapper,
    private val sseBroadcaster: NotificationSSEBroadcaster,
    private val notificationProducer: NotificationProducer,
) {
    suspend fun convertEventAndSave(
        request: KafkaEvent<*>
    ): NotificationEntity {
        val notificationRequest = when (request.eventType) {
            NotificationType.MEMBER_JOINED.toString() ->
                objectMapper.convertValue(request.data, NotificationEventRequest.MemberJoined::class.java)

            NotificationType.ROLE_MODIFIED.toString() ->
                objectMapper.convertValue(request.data, NotificationEventRequest.RoleModified::class.java)

            NotificationType.QNA_CREATED.toString() ->
                objectMapper.convertValue(request.data, NotificationEventRequest.QnaCreated::class.java)

            NotificationType.QNA_ANSWERED.toString() ->
                objectMapper.convertValue(request.data, NotificationEventRequest.QnaAnswered::class.java)

            NotificationType.ENCODING_STATUS.toString() ->
                objectMapper.convertValue(request.data, NotificationEventRequest.EncodingStatus::class.java)

            NotificationType.LEVELTEST_REPORT_REQUEST.toString() ->
                objectMapper.convertValue(request.data, NotificationEventRequest.LevelTestReportRequested::class.java)

            else -> throw BaseException(NotificationError.NO_SUCH_NOTIFICATION_TYPE)
        }
        return notificationRepository.save(notificationEntityFactory.create(notificationRequest))
    }

    suspend fun subscribe(memberId: Long): Flow<NotificationEntity> {
        return sseBroadcaster.subscribe(memberId)
    }

    suspend fun findById(memberId: Long): List<NotificationEntity> {
        return notificationRepository.findByMemberId(memberId)
    }

    @Transactional
    suspend fun readNotification(memberId: Long, notificationId: UUID) {
        val notifications = notificationRepository.findByMemberIdAndId(memberId, notificationId)
        if (notifications.isNotEmpty())
            notificationRepository.deleteById(notificationId)
    }

    suspend fun sendNotification(request: KafkaEvent<NotificationEventRequest.QnaCreated>) {
        notificationProducer.sendNotificationEvent(request)
    }

    fun getServerStatus(): Map<String, Any> {
        return mapOf(
            "activeSubscribers" to sseBroadcaster.getActiveSubscriberCount(),
            "status" to "RUNNING"
        )
    }
}
