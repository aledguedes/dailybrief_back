CREATE TABLE tbl_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE tbl_post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    image VARCHAR(255),
    author VARCHAR(255),
    category VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    published_at TIMESTAMP,
    read_time VARCHAR(50)
);

CREATE TABLE tbl_post_title (
    post_id BIGINT,
    lang VARCHAR(10),
    title VARCHAR(500),
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id)
);

CREATE TABLE tbl_post_excerpt (
    post_id BIGINT,
    lang VARCHAR(10),
    excerpt TEXT,
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id)
);

CREATE TABLE tbl_post_content (
    post_id BIGINT,
    lang VARCHAR(10),
    content TEXT,
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id)
);

CREATE TABLE tbl_post_meta_description (
    post_id BIGINT,
    lang VARCHAR(10),
    meta_description VARCHAR(500),
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id)
);

CREATE TABLE tbl_post_affiliate_link (
    post_id BIGINT,
    lang VARCHAR(10),
    affiliate_link VARCHAR(255),
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id)
);

CREATE TABLE tbl_post_tags (
    post_id BIGINT,
    tags VARCHAR(255),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id)
);

CREATE TABLE tbl_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    action VARCHAR(255) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

-- Dados iniciais
INSERT INTO tbl_user (id, email, password) VALUES 
(1, 'admin@dailybrief.com', '$2a$12$7xVaaik7.m2w2ez7.A4sTupvCmIad.wgXSkOPaAlLid44BJfwahUC'),
(2, 'admin_py@dailybrief.com', '$2a$12$Y.mTzt9L6Jvn/qhyFjREMOha36fs0yp.KSAYrjU1MK74yUaC1F9j2');
