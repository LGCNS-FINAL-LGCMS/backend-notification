DROP TABLE IF EXISTS notifications;

CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    notification_type VARCHAR(50) NOT NULL,
    member_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    web_path VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_id ON notifications(id);
CREATE INDEX IF NOT EXISTS idx_member_id ON notifications(member_id);
CREATE INDEX IF NOT EXISTS idx_notifications_type ON notifications(notification_type);
CREATE INDEX IF NOT EXISTS idx_created_at ON notifications(created_at);
