CREATE TABLE IF NOT EXISTS tbl_trending_topic_suggestions (
    id BIGSERIAL PRIMARY KEY,
    topic_name VARCHAR(255) NOT NULL,
    source VARCHAR(50) NOT NULL,
    relevance_reason VARCHAR(1000) NOT NULL,
    url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);