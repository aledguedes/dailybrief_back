CREATE TABLE post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    imageUrl VARCHAR(255),
    author VARCHAR(255),
    tags VARCHAR(255)
);

CREATE TABLE post (
    id BIGINT PRIMARY KEY,
    image_url VARCHAR(255),
    author VARCHAR(255),
    category VARCHAR(255),
    status VARCHAR(50),
    published_at TIMESTAMP
);
CREATE TABLE post_title (post_id BIGINT, lang VARCHAR(10), title VARCHAR(255));
CREATE TABLE post_content (post_id BIGINT, lang VARCHAR(10), content TEXT);