ALTER TABLE `article` DROP FOREIGN KEY `fk_article_user_1`;
ALTER TABLE `message` DROP FOREIGN KEY `fk_message_user_1`;
ALTER TABLE `file` DROP FOREIGN KEY `fk_file_user_1`;
ALTER TABLE `comment` DROP FOREIGN KEY `fk_comment_user_1`;
ALTER TABLE `tag` DROP FOREIGN KEY `fk_tag_user_1`;
ALTER TABLE `about` DROP FOREIGN KEY `fk_about_user_1`;
ALTER TABLE `article_file` DROP FOREIGN KEY `fk_article_file_user_1`;
ALTER TABLE `article_tag` DROP FOREIGN KEY `fk_article_tag_user_1`;
ALTER TABLE `comment` DROP FOREIGN KEY `fk_comment_article_1`;
ALTER TABLE `article_tag` DROP FOREIGN KEY `fk_article_tag_article_1`;
ALTER TABLE `article_tag` DROP FOREIGN KEY `fk_article_tag_tag_1`;
ALTER TABLE `article_file` DROP FOREIGN KEY `fk_article_file_article_1`;
ALTER TABLE `article_file` DROP FOREIGN KEY `fk_article_file_file_1`;

DROP TABLE `user`;
DROP TABLE `tag`;
DROP TABLE `article`;
DROP TABLE `comment`;
DROP TABLE `message`;
DROP TABLE `about`;
DROP TABLE `article_tag`;
DROP TABLE `file`;
DROP TABLE `article_file`;

CREATE TABLE `user` (
`id` varchar(32) NOT NULL,
`username` varchar(20) NULL COMMENT '用户名',
`password` varchar(20) NULL COMMENT '密码',
`salt` varchar(20) NULL COMMENT '密码加密的盐',
`hash_count` int(1) NULL COMMENT '加密次数',
`avatar_file_id` varchar(32) NULL COMMENT '头像文件id',
`phone_number` varchar(11) NULL COMMENT '手机号码',
`email` varchar(50) NULL COMMENT '邮箱',
`created_time` datetime NULL COMMENT '注册时间',
`modified_time` datetime NULL COMMENT '修改时间',
`available` int(1) NULL COMMENT '用户是否可用。-1-删除，0-锁定，1-正常',
PRIMARY KEY (`id`) 
)
COMMENT = '用户信息表';
CREATE TABLE `tag` (
`id` varchar(32) NOT NULL,
`name` varchar(30) NULL COMMENT '标签名字',
`user_id` varchar(32) NULL COMMENT '标签所属用户',
`created_time` datetime NULL COMMENT '标签创建时间',
`available` int(255) NULL COMMENT '标签是否可用。-1-删除，0-禁用，1-正常',
PRIMARY KEY (`id`) 
)
COMMENT = '文章标签';
CREATE TABLE `article` (
`id` varchar(32) NOT NULL,
`user_id` varchar(32) NULL COMMENT '博文所属用户id',
`title` varchar(50) NULL COMMENT '博文标题',
`content` blob NULL COMMENT '博文内容',
`is_draft` int(1) NULL COMMENT '是否是草稿。单个用户最多只会有一个草稿。0-否，1-是',
`read_count` int(11) NULL COMMENT '阅读次数',
`praise_count` int(11) NULL COMMENT '点赞次数',
`created_time` datetime NULL COMMENT '创建时间',
`publish_time` datetime NULL COMMENT '发布时间',
`modified_time` datetime NULL COMMENT '修改时间',
`available` int(1) NULL COMMENT '博文状态。-1-删除，0-不可见，1-正常',
PRIMARY KEY (`id`) 
)
COMMENT = '博文内容';
CREATE TABLE `comment` (
`id` varchar(32) NOT NULL,
`article_id` varchar(32) NULL COMMENT '评论所属博文id',
`user_name` varchar(32) NULL COMMENT '评论人名字，外部用户使用，和user_id二选一',
`user_id` varchar(32) NULL COMMENT '用户id，内部用户使用，和user_name二选一',
`content` blob NULL COMMENT '评论内容',
`praise_count` int(11) NULL COMMENT '点赞次数',
`created_time` datetime NULL COMMENT '评论时间',
`available` int(1) NULL COMMENT '评论状态。-1-删除，0-不可见，1-正常',
PRIMARY KEY (`id`) 
)
COMMENT = '评论';
CREATE TABLE `message` (
`id` varchar(32) NOT NULL,
`user_name` varchar(32) NULL COMMENT '留言人名字，外部用户使用，和user_id二选一',
`user_id` varchar(32) NULL COMMENT '留言人id，内部用户使用。和user_name二选一',
`content` blob NULL COMMENT '留言内容',
`praise_count` int(11) NULL COMMENT '点赞次数',
`created_time` datetime NULL COMMENT '留言时间',
`available` int(1) NULL COMMENT '留言状态。-1-删除，0-不可见，1-正常',
PRIMARY KEY (`id`) 
)
COMMENT = '留言';
CREATE TABLE `about` (
`id` varchar(32) NOT NULL,
`user_id` varchar(32) NULL COMMENT '关于所属用户id',
`content` blob NULL COMMENT '关于的内容',
`created_time` datetime NULL COMMENT '关于的填写时间',
`available` int(1) NULL COMMENT '关于状态。-1-删除，0-不可见，1-可见。此表中每个用户最多有1个是可见状态',
PRIMARY KEY (`id`) 
)
COMMENT = '关于';
CREATE TABLE `article_tag` (
`id` varchar(32) NOT NULL,
`article_id` varchar(32) NULL COMMENT '博文id',
`tag_id` varchar(32) NULL COMMENT '标签id',
`user_id` varchar(32) NULL COMMENT '文章-标签对应关系所属用户',
`created_time` datetime NULL COMMENT '关联时间',
PRIMARY KEY (`id`) 
)
COMMENT = '博文与标签的对应关系';
CREATE TABLE `file` (
`id` varchar(32) NOT NULL,
`original_name` varchar(100) NULL COMMENT '原始名字（不包含后缀名）',
`save_name` varchar(100) NULL COMMENT '服务器上文件名字（不包含后缀名）',
`extension_name` varchar(10) NULL COMMENT '扩展名（不包含.）',
`file_type` varchar(50) NULL COMMENT '文件类型（类似 image/jpeg)',
`size` bigint(32) NULL COMMENT '文件大小（字节）',
`user_id` varchar(32) NULL COMMENT '上传用户id',
`created_time` datetime NULL COMMENT '上传时间',
`available` int(1) NULL COMMENT '文件状态。-1-删除，0-不可见（临时文件），1-正常',
PRIMARY KEY (`id`) 
)
COMMENT = '上传的文件';
CREATE TABLE `article_file` (
`id` varchar(32) NOT NULL,
`article_id` varchar(32) NULL COMMENT '文章id',
`file_id` varchar(32) NULL COMMENT '文件id',
`user_id` varchar(32) NULL COMMENT '关联关系所属用户id',
`created_time` datetime NULL COMMENT '关联时的时间',
PRIMARY KEY (`id`) 
)
COMMENT = '文章对应的文件，主要用于删除文章时同步删除文件';

ALTER TABLE `article` ADD CONSTRAINT `fk_article_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `message` ADD CONSTRAINT `fk_message_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `file` ADD CONSTRAINT `fk_file_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `comment` ADD CONSTRAINT `fk_comment_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `tag` ADD CONSTRAINT `fk_tag_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `about` ADD CONSTRAINT `fk_about_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `article_file` ADD CONSTRAINT `fk_article_file_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `article_tag` ADD CONSTRAINT `fk_article_tag_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `comment` ADD CONSTRAINT `fk_comment_article_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `article_tag` ADD CONSTRAINT `fk_article_tag_article_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `article_tag` ADD CONSTRAINT `fk_article_tag_tag_1` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `article_file` ADD CONSTRAINT `fk_article_file_article_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `article_file` ADD CONSTRAINT `fk_article_file_file_1` FOREIGN KEY (`file_id`) REFERENCES `file` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

