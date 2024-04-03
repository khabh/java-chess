GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS chess DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE IF NOT EXISTS chess_test DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

# 애플리케이션 실행용 테이블 초기화
USE chess;

CREATE TABLE IF NOT EXISTS boards
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS moves
(
    id          INT NOT NULL AUTO_INCREMENT,
    board_id    INT NOT NULL,
    source_file INT NOT NULL,
    source_rank INT NOT NULL,
    target_file INT NOT NULL,
    target_rank INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (board_id) REFERENCES boards (id)
);

CREATE TABLE IF NOT EXISTS winners
(
    id       INT         NOT NULL AUTO_INCREMENT,
    color    VARCHAR(50) NOT NULL,
    board_id INT         NOT NULL UNIQUE,
    PRIMARY KEY (id),
    FOREIGN KEY (board_id) REFERENCES boards (id)
);

