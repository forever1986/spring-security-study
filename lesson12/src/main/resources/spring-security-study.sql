-- spring_security_study.t_user 用户表
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
 `username` varchar(100) NOT NULL,
 `password` varchar(100) NOT NULL,
 `email` varchar(100) DEFAULT NULL,
 `phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- spring_security_study.t_permission 权限码表
CREATE TABLE `t_permission` (
 `id` bigint NOT NULL AUTO_INCREMENT,
  `permission_code` varchar(200) NOT null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- spring_security_study.t_role 角色表
CREATE TABLE `t_role` (
 `id` bigint NOT NULL AUTO_INCREMENT,
 `role_code` varchar(100) NOT null,
  `role_name` varchar(100) NOT null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- spring_security_study.t_role_permission 角色与权限关联表
CREATE TABLE `t_role_permission` (
 `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- spring_security_study.t_user_role 用户与角色关联表
CREATE TABLE `t_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 插入数据
INSERT INTO spring_security_study.t_user (id, username, password, email, phone) VALUES(4, 'test', '{noop}1234', 'test@demo.com', '13788888888');
INSERT INTO spring_security_study.t_user (id, username, password, email, phone) VALUES(5, 'admin', '{noop}1234', 'admin@demo.com', '13766666666');
INSERT INTO spring_security_study.t_permission (id, permission_code) VALUES(1, 'PRODUCT_LIST');
INSERT INTO spring_security_study.t_permission (id, permission_code) VALUES(2, 'PRODUCT_EDIT');
INSERT INTO spring_security_study.t_role (id, role_code, role_name) VALUES(1, 'ADMIN', '管理员');
INSERT INTO spring_security_study.t_role (id, role_code, role_name) VALUES(2, 'USER', '普通用户');
INSERT INTO spring_security_study.t_role_permission (id, role_id, permission_id) VALUES(1, 1, 1);
INSERT INTO spring_security_study.t_role_permission (id, role_id, permission_id) VALUES(2, 1, 2);
INSERT INTO spring_security_study.t_role_permission (id, role_id, permission_id) VALUES(3, 2, 1);
INSERT INTO spring_security_study.t_user_role (id, user_id, role_id) VALUES(1, 4, 2);
INSERT INTO spring_security_study.t_user_role (id, user_id, role_id) VALUES(2, 5, 1);
