package com.lgcms.notification.event.kafka.dto

sealed class NotificationEventRequest {
    data class MemberJoined(
        val memberId: Long,
        val nickname: String,
    ) : NotificationEventRequest()

    data class RoleModified(
        val memberId: Long,
        val nickname: String,
        val memberRoleName: String,
    ) : NotificationEventRequest()

    data class QnaCreated(
        val memberId: Long,
        val qnaId: Long,
        val lectureName: String,
    ) : NotificationEventRequest()

    data class QnaAnswered(
        val memberId: Long,
        val qnaId: Long,
        val questionTitle: String,
    ) : NotificationEventRequest()

    data class EncodingStatus(
        val memberId: Long,
        val lectureName: String,
        val lectureId: String,
        val status: String,
    ) : NotificationEventRequest()

    data class LevelTestReportRequested(
        val memberId: Long,
        val studentReportId: Long,
    ) : NotificationEventRequest()
}