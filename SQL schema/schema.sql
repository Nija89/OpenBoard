CREATE DATABASE OpenBoard;
USE OpenBoard;

-- User table
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN DEFAULT TRUE,
    description TEXT
);

-- Role table
CREATE TABLE role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO role (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO role (role_name) VALUES ('ROLE_MODERATOR');
INSERT INTO role (role_name) VALUES ('ROLE_MEMBER');

-- User-Role mapping table
CREATE TABLE user_role (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Post table
CREATE TABLE post (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    show_post BOOLEAN DEFAULT TRUE,  -- TRUE for public, FALSE for private
    likes INT DEFAULT 0,
    dislikes INT DEFAULT 0,
    user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Post-Like Mapping Table
CREATE TABLE post_like (
    post_id INT,
    user_id INT,
    PRIMARY KEY (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES post(id) ,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Post-DisLike Mapping Table
CREATE TABLE post_dislike (
    post_id INT,
    user_id INT,
    PRIMARY KEY (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES post(id) ,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

