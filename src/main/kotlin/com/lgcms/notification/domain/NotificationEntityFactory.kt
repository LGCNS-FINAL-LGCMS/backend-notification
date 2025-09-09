package com.lgcms.notification.domain

import com.lgcms.notification.event.kafka.dto.NotificationEventRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NotificationEntityFactory(
    @Value("\${front-routes.student-main-page}")
    private val studentMainPage: String,
    @Value("\${front-routes.lecturer-main-page}")
    private val lecturerMainPage: String,
    @Value("\${front-routes.qna-page}")
    private val qnaPage: String,
    @Value("\${front-routes.lecture-edit-page}")
    private val lectureEditPage: String,
    @Value("\${front-routes.leveltest-report-page}")
    private val leveltestReportPage: String,
) {
    fun create(request: NotificationEventRequest): NotificationEntity {
        return when (request) {
            is NotificationEventRequest.MemberJoined -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.MEMBER_JOINED,
                content = "${cutName(request.nickname)}님 회원가입을 축하해요",
                webPath = studentMainPage,
            )

            is NotificationEventRequest.RoleModified -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.ROLE_MODIFIED,
                content = "${cutName(request.nickname)}님 ${request.memberRoleName}가 됐어요",
                webPath = lecturerMainPage,
            )

            is NotificationEventRequest.QnaCreated -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.QNA_CREATED,
                content = "${cutName(request.lectureName)}에서 질문이 등록됐어요",
                webPath = String.format(qnaPage, request.qnaId),
            )

            is NotificationEventRequest.QnaAnswered -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.QNA_ANSWERED,
                content = "${cutName(request.questionTitle)} 질문에 답변이 등록됐어요",
                webPath = String.format(qnaPage, request.qnaId),
            )

            is NotificationEventRequest.EncodingStatus -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.ENCODING_STATUS,
                content = "${cutName(request.lectureName)} 강의 영상 업로드를 ${request.status}했어요",
                webPath = String.format(lectureEditPage, request.lectureId),
            )

            is NotificationEventRequest.LevelTestReportRequested -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.LEVELTEST_REPORT_REQUESTED,
                content = "레벨 테스트 레포트가 작성됐어요",
                webPath = String.format(leveltestReportPage, request.studentReportId),
            )
        }
    }

    fun cutName(name: String): String {
        return if (name.length > 10) name.substring(0, 10) + "..." else name
    }
}