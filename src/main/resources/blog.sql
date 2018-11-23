/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 23/11/2018 10:07:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for about
-- ----------------------------
DROP TABLE IF EXISTS `about`;
CREATE TABLE `about` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '关于所属用户id',
  `content` text COMMENT '关于的内容',
  `created_time` datetime DEFAULT NULL COMMENT '关于的填写时间',
  `available` int(1) DEFAULT NULL COMMENT '关于状态。-1-删除，0-不可见，1-可见。此表中每个用户最多有1个是可见状态',
  PRIMARY KEY (`id`),
  KEY `fk_about_user_1` (`user_id`),
  CONSTRAINT `fk_about_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关于';

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '博文所属用户id',
  `title` varchar(50) DEFAULT NULL COMMENT '博文标题',
  `content` text COMMENT '博文内容',
  `is_draft` int(1) DEFAULT NULL COMMENT '是否是草稿。单个用户最多只会有一个草稿。0-否，1-是',
  `read_count` int(11) DEFAULT NULL COMMENT '阅读次数',
  `praise_count` int(11) DEFAULT NULL COMMENT '点赞次数',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  `available` int(1) DEFAULT NULL COMMENT '博文状态。-1-删除，0-不可见，1-正常',
  PRIMARY KEY (`id`),
  KEY `fk_article_user_1` (`user_id`),
  CONSTRAINT `fk_article_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博文内容';

-- ----------------------------
-- Table structure for article_file
-- ----------------------------
DROP TABLE IF EXISTS `article_file`;
CREATE TABLE `article_file` (
  `id` varchar(32) NOT NULL,
  `article_id` varchar(32) DEFAULT NULL COMMENT '文章id',
  `file_id` varchar(32) DEFAULT NULL COMMENT '文件id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '关联关系所属用户id',
  `created_time` datetime DEFAULT NULL COMMENT '关联时的时间',
  PRIMARY KEY (`id`),
  KEY `fk_article_file_user_1` (`user_id`),
  KEY `fk_article_file_article_1` (`article_id`),
  KEY `fk_article_file_file_1` (`file_id`),
  CONSTRAINT `fk_article_file_article_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_article_file_file_1` FOREIGN KEY (`file_id`) REFERENCES `file` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_article_file_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章对应的文件，主要用于删除文章时同步删除文件';

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag` (
  `id` varchar(32) NOT NULL,
  `article_id` varchar(32) DEFAULT NULL COMMENT '博文id',
  `tag_id` varchar(32) DEFAULT NULL COMMENT '标签id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '文章-标签对应关系所属用户',
  `created_time` datetime DEFAULT NULL COMMENT '关联时间',
  PRIMARY KEY (`id`),
  KEY `fk_article_tag_user_1` (`user_id`),
  KEY `fk_article_tag_article_1` (`article_id`),
  KEY `fk_article_tag_tag_1` (`tag_id`),
  CONSTRAINT `fk_article_tag_article_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_article_tag_tag_1` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_article_tag_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博文与标签的对应关系';

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` varchar(32) NOT NULL,
  `article_id` varchar(32) DEFAULT NULL COMMENT '评论所属博文id',
  `user_name` varchar(32) DEFAULT NULL COMMENT '评论人名字，外部用户使用，和user_id二选一',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id，内部用户使用，和user_name二选一',
  `content` text COMMENT '评论内容',
  `praise_count` int(11) DEFAULT NULL COMMENT '点赞次数',
  `created_time` datetime DEFAULT NULL COMMENT '评论时间',
  `available` int(1) DEFAULT NULL COMMENT '评论状态。-1-删除，0-不可见，1-正常',
  PRIMARY KEY (`id`),
  KEY `fk_comment_user_1` (`user_id`),
  KEY `fk_comment_article_1` (`article_id`),
  CONSTRAINT `fk_comment_article_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论';

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `id` varchar(32) NOT NULL,
  `original_name` varchar(100) DEFAULT NULL COMMENT '原始名字（不包含后缀名）',
  `save_name` varchar(100) DEFAULT NULL COMMENT '服务器上文件名字（不包含后缀名）',
  `extension_name` varchar(10) DEFAULT NULL COMMENT '扩展名（不包含.）',
  `file_type` varchar(50) DEFAULT NULL COMMENT '文件类型（类似 image/jpeg)',
  `size` bigint(32) DEFAULT NULL COMMENT '文件大小（字节）',
  `user_id` varchar(32) DEFAULT NULL COMMENT '上传用户id',
  `created_time` datetime DEFAULT NULL COMMENT '上传时间',
  `available` int(1) DEFAULT NULL COMMENT '文件状态。-1-删除，0-不可见（临时文件），1-正常',
  PRIMARY KEY (`id`),
  KEY `fk_file_user_1` (`user_id`),
  CONSTRAINT `fk_file_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='上传的文件';

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` varchar(32) NOT NULL,
  `user_name` varchar(32) DEFAULT NULL COMMENT '留言人名字，外部用户使用，和user_id二选一',
  `user_id` varchar(32) DEFAULT NULL COMMENT '留言人id，内部用户使用。和user_name二选一',
  `content` text COMMENT '留言内容',
  `praise_count` int(11) DEFAULT NULL COMMENT '点赞次数',
  `created_time` datetime DEFAULT NULL COMMENT '留言时间',
  `available` int(1) DEFAULT NULL COMMENT '留言状态。-1-删除，0-不可见，1-正常',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='留言';

-- ----------------------------
-- Table structure for praise
-- ----------------------------
DROP TABLE IF EXISTS `praise`;
CREATE TABLE `praise` (
  `id` varchar(32) NOT NULL,
  `article_id` varchar(32) DEFAULT NULL COMMENT '被赞文章id，和评论id二选一，不可同时有值',
  `comment_id` varchar(32) DEFAULT NULL COMMENT '评论id，和文章id二选一，不可同时有值',
  `user_id` varchar(32) DEFAULT NULL COMMENT '点赞人id。如果没登录则没有',
  `created_time` datetime DEFAULT NULL COMMENT '点赞时间',
  PRIMARY KEY (`id`),
  KEY `fk_praise_article_1` (`article_id`),
  KEY `fk_praise_comment_1` (`comment_id`),
  CONSTRAINT `fk_praise_article_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_praise_comment_1` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章、评论的点赞记录表';

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` varchar(32) NOT NULL,
  `name` varchar(30) DEFAULT NULL COMMENT '标签名字',
  `user_id` varchar(32) DEFAULT NULL COMMENT '标签所属用户',
  `created_time` datetime DEFAULT NULL COMMENT '标签创建时间',
  `available` int(255) DEFAULT NULL COMMENT '标签是否可用。-1-删除，0-禁用，1-正常',
  PRIMARY KEY (`id`),
  KEY `fk_tag_user_1` (`user_id`),
  CONSTRAINT `fk_tag_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章标签';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(32) NOT NULL,
  `username` varchar(20) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '密码加密的盐',
  `hash_count` int(1) DEFAULT NULL COMMENT '加密次数',
  `avatar_file_id` varchar(32) DEFAULT NULL COMMENT '头像文件id',
  `phone_number` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `created_time` datetime DEFAULT NULL COMMENT '注册时间',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  `available` int(1) DEFAULT NULL COMMENT '用户是否可用。-1-删除，0-锁定，1-正常',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

SET FOREIGN_KEY_CHECKS = 1;
