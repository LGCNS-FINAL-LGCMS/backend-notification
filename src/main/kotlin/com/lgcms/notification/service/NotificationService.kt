package com.lgcms.notification.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.lgcms.notification.common.dto.exception.BaseException
import com.lgcms.notification.common.dto.exception.NotificationError
import com.lgcms.notification.common.kafka.dto.KafkaEvent
import com.lgcms.notification.domain.NotificationEntity
import com.lgcms.notification.domain.NotificationEntityFactory
import com.lgcms.notification.domain.NotificationType
import com.lgcms.notification.event.kafka.dto.NotificationEventRequest
import com.lgcms.notification.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val notificationEntityFactory: NotificationEntityFactory,
    private val objectMapper: ObjectMapper,
    private val sseBroadcaster: NotificationSSEBroadcaster,
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
}
