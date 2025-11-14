CREATE TABLE tbl_users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE tbl_status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    display_name VARCHAR(100),
    bg_class VARCHAR(50),
    text_class VARCHAR(50)
);

CREATE TABLE tbl_categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255),
    target_audience VARCHAR(50)
);

CREATE TABLE tbl_posts (
    id VARCHAR(36) PRIMARY KEY,
    image VARCHAR(255),
    author VARCHAR(255),
    category_id INTEGER REFERENCES tbl_categories(id) ON DELETE SET NULL,
    status_id INTEGER REFERENCES tbl_status(id) ON DELETE SET NULL,
    published_at TIMESTAMP WITH TIME ZONE,
    read_time VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    url VARCHAR(255) NOT NULL,
    public_id VARCHAR(255) NOT NULL UNIQUE,
    post_id VARCHAR(36) REFERENCES tbl_posts(id) ON DELETE CASCADE
);


CREATE TABLE tbl_post_title (
    post_id VARCHAR(36),
    lang VARCHAR(10),
    title VARCHAR(500),
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_posts(id) ON DELETE CASCADE
);

CREATE TABLE tbl_post_excerpt (
    post_id VARCHAR(36),
    lang VARCHAR(10),
    excerpt TEXT,
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_posts(id) ON DELETE CASCADE
);

CREATE TABLE tbl_post_content (
    post_id VARCHAR(36),
    lang VARCHAR(10),
    content TEXT,
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_posts(id) ON DELETE CASCADE
);

CREATE TABLE tbl_post_meta_description (
    post_id VARCHAR(36),
    lang VARCHAR(10),
    meta_description VARCHAR(500),
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_posts(id) ON DELETE CASCADE
);

CREATE TABLE tbl_post_affiliate_link (
    post_id VARCHAR(36),
    lang VARCHAR(10),
    affiliate_link VARCHAR(255),
    PRIMARY KEY (post_id, lang),
    FOREIGN KEY (post_id) REFERENCES tbl_posts(id) ON DELETE CASCADE
);

CREATE TABLE tbl_post_tags (
    id SERIAL PRIMARY KEY,
    post_id VARCHAR(36),
    tags VARCHAR(255),
    FOREIGN KEY (post_id) REFERENCES tbl_posts(id) ON DELETE CASCADE
);


CREATE TABLE tbl_logs (
    id SERIAL PRIMARY KEY,
    action VARCHAR(255) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_automation_configs (
    task_id VARCHAR(36) PRIMARY KEY,
    status_id INTEGER REFERENCES tbl_status(id),
    search_factors JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_materials (
    task_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    status_id INTEGER REFERENCES tbl_status(id),
    post_id VARCHAR(36),
    theme VARCHAR(500),
    content_type VARCHAR(100),
    suggested_image_prompt TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_materials_post
        FOREIGN KEY (post_id)
        REFERENCES tbl_posts(id)
        ON DELETE SET NULL
);

CREATE TABLE tbl_raw_materials (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    task_id VARCHAR(36) REFERENCES tbl_materials(task_id) ON DELETE CASCADE,
    url TEXT NOT NULL,
    content TEXT, 
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tbl_material_source (
    id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(), 
    task_id VARCHAR(36) NOT NULL REFERENCES tbl_materials(task_id) ON DELETE CASCADE,    
    url TEXT NOT NULL,    
    status VARCHAR(50) NOT NULL,
    raw_material_id VARCHAR(36) REFERENCES tbl_raw_materials(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_material_source_task_id ON tbl_material_source (task_id);

INSERT INTO tbl_users (email, password) VALUES 
('admin@dailybrief.com', '$2a$12$7xVaaik7.m2w2ez7.A4sTupvCmIad.wgXSkOPaAlLid44BJfwahUC'),
('admin_py@dailybrief.com', '$2a$12$Y.mTzt9L6Jvn/qhyFjREMOha36fs0yp.KSAYrjU1MK74yUaC1F9j2');

INSERT INTO tbl_status (id, name, display_name, bg_class, text_class) VALUES
(1, 'PENDING', 'Pendente (Início)', 'bg-blue-100', 'text-blue-700'),
(2, 'RAW_COLLECTED', 'Matéria Bruta Salva', 'bg-yellow-100', 'text-yellow-700'),
(3, 'COLLECTION_FAILED', 'Falha na Coleta', 'bg-red-100', 'text-red-700'),
(4, 'PENDING_GENERATION', 'Aguardando IA', 'bg-indigo-100', 'text-indigo-700'),
(5, 'GENERATED', 'Conteúdo Gerado', 'bg-green-100', 'text-green-700'),
(6, 'FAILED_GENERATION', 'Falha na Geração', 'bg-red-200', 'text-red-800'),
(7, 'PENDING_IMAGE', 'Aguardando Imagem', 'bg-purple-100', 'text-purple-700'),
(8, 'FAILED_IMAGE', 'Falha na Imagem', 'bg-orange-100', 'text-orange-700'),
(9, 'IMAGE_GENERATED', 'Imagem Gerada', 'bg-teal-100', 'text-teal-700'),
(10, 'PENDING_PUBLISH', 'Aguardando Publicação', 'bg-gray-200', 'text-gray-800'),
(11, 'PUBLISHED', 'Publicado com Sucesso', 'bg-green-200', 'text-green-800'),
(12, 'FAILED_PUBLISH', 'Falha na Publicação', 'bg-red-300', 'text-red-900'),
(13, 'EDITED', 'Editado Manualmente', 'bg-pink-100', 'text-pink-700'),
(14, 'PENDING_COLLECTION', 'Aguardando Coleta', 'bg-blue-200', 'text-blue-800'),
(15, 'COMPLETED', 'Concluído', 'bg-green-300', 'text-green-900'),
(16, 'REJECTED', 'Rejeitado', 'bg-red-300', 'text-red-900')
ON CONFLICT (id) DO NOTHING;

INSERT INTO tbl_categories (id, name, description, target_audience) VALUES
(1, 'TECH', 'Notícias e tendências sobre inovação, IA, gadgets e avanços tecnológicos.', 'MISTO'),
(2, 'BUSINESS', 'Análises de mercado, startups, economia e empreendedorismo.', 'MISTO'),
(3, 'SCIENCE', 'Descobertas científicas, pesquisas e inovações no campo científico.', 'LEIGO'),
(4, 'POLITICS', 'Atualizações sobre governos, eleições e geopolítica global.', 'MISTO'),
(5, 'HEALTH', 'Artigos sobre bem-estar, medicina, alimentação e saúde mental.', 'LEIGO'),
(6, 'ENVIRONMENT', 'Sustentabilidade, mudanças climáticas e energia limpa.', 'MISTO'),
(7, 'CULTURE', 'Entretenimento, arte, cinema, música e sociedade.', 'MISTO'),
(8, 'SPORTS', 'Cobertura e análises esportivas nacionais e internacionais.', 'LEIGO'),
(9, 'TECH_POLICY', 'Debates sobre regulação tecnológica, privacidade e ética digital.', 'TÉCNICO'),
(10, 'AI', 'Conteúdo sobre IA, aprendizado de máquina e automação.', 'TÉCNICO')
ON CONFLICT (id) DO NOTHING;