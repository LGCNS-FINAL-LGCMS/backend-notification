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
                content = "${request.nickname}님 회원가입을 축하합니다.",
                webPath = studentMainPage,
            )

            is NotificationEventRequest.RoleModified -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.ROLE_MODIFIED,
                content = "${request.nickname}님 ${request.memberRoleName}가 되었습니다",
                webPath = lecturerMainPage,
            )

            is NotificationEventRequest.QnaCreated -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.QNA_CREATED,
                content = "${request.lectureName}에서 질문이 등록되었습니다",
                webPath = String.format(qnaPage, request.qnaId),
            )

            is NotificationEventRequest.QnaAnswered -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.QNA_ANSWERED,
                content = "${request.questionTitle} 질문에 답변이 등록되었습니다",
                webPath = String.format(qnaPage, request.qnaId),
            )

            is NotificationEventRequest.EncodingStatus -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.ENCODING_STATUS,
                content = "${request.lectureName} 강의 영상 업로드를 ${request.status}헀습니다",
                webPath = String.format(lectureEditPage, request.lectureId),
            )

            is NotificationEventRequest.LevelTestReportRequested -> NotificationEntity(
                memberId = request.memberId,
                notificationType = NotificationType.LEVELTEST_REPORT_REQUEST,
                content = "레벨 테스트 레포트 페이지가 완성되었습니다",
                webPath = String.format(leveltestReportPage, request.studentReportId),
            )
        }
    }
}