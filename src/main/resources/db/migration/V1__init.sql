-- Tabela de usuários
CREATE TABLE tbl_user (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Tabela de posts
CREATE TABLE tbl_post (
    id SERIAL PRIMARY KEY,
    image VARCHAR(255),
    author VARCHAR(255),
    category VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    published_at TIMESTAMP,
    read_time VARCHAR(50)
);

-- Títulos dos posts por idioma
CREATE TABLE tbl_post_title (
    post_id BIGINT,
    lang VARCHAR(10),
    title VARCHAR(500),
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id) ON DELETE CASCADE
);

-- Excertos dos posts por idioma
CREATE TABLE tbl_post_excerpt (
    post_id BIGINT,
    lang VARCHAR(10),
    excerpt TEXT,
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id) ON DELETE CASCADE
);

-- Conteúdo dos posts por idioma
CREATE TABLE tbl_post_content (
    post_id BIGINT,
    lang VARCHAR(10),
    content TEXT,
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id) ON DELETE CASCADE
);

-- Meta description dos posts por idioma
CREATE TABLE tbl_post_meta_description (
    post_id BIGINT,
    lang VARCHAR(10),
    meta_description VARCHAR(500),
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id) ON DELETE CASCADE
);

-- Links afiliados por idioma
CREATE TABLE tbl_post_affiliate_link (
    post_id BIGINT,
    lang VARCHAR(10),
    affiliate_link VARCHAR(255),
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id) ON DELETE CASCADE
);

-- Tags dos posts
CREATE TABLE tbl_post_tags (
    id SERIAL PRIMARY KEY,
    post_id BIGINT,
    tags VARCHAR(255),
    FOREIGN KEY (post_id) REFERENCES tbl_post(id) ON DELETE CASCADE
);

-- Logs de ações
CREATE TABLE tbl_log (
    id SERIAL PRIMARY KEY,
    action VARCHAR(255) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Requisições de automação
CREATE TABLE automation_requests (
    id SERIAL PRIMARY KEY,
    output_format VARCHAR(50) NOT NULL,
    theme VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Inserção de dados iniciais
INSERT INTO tbl_user (email, password) VALUES 
('admin@dailybrief.com', '$2a$12$7xVaaik7.m2w2ez7.A4sTupvCmIad.wgXSkOPaAlLid44BJfwahUC'),
('admin_py@dailybrief.com', '$2a$12$Y.mTzt9L6Jvn/qhyFjREMOha36fs0yp.KSAYrjU1MK74yUaC1F9j2');
