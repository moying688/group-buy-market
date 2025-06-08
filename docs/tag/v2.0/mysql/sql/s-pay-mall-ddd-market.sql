/*
 Navicat Premium Data Transfer

 Source Server         : moying
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : s-pay-mall-ddd-market

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 08/06/2025 13:22:42
*/


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

drop database if exists `s-pay-mall-ddd-market`;
create database if not exists `s-pay-mall-ddd-market` default character set utf8mb4;
use `s-pay-mall-ddd-market`;

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order`
(
    `id`                      int UNSIGNED                                                   NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '用户ID',
    `product_id`              varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '商品ID',
    `product_name`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '商品名称',
    `order_id`                varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '订单ID',
    `order_time`              datetime                                                       NOT NULL COMMENT '下单时间',
    `total_amount`            decimal(8, 2) UNSIGNED                                         NULL     DEFAULT NULL COMMENT '订单金额',
    `status`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '订单状态；create-创建完成、pay_wait-等待支付、pay_success-支付成功、deal_done-交易完成、close-订单关单',
    `pay_url`                 varchar(2014) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '支付信息',
    `pay_time`                datetime                                                       NULL     DEFAULT NULL COMMENT '支付时间',
    `market_type`             tinyint(1)                                                     NULL     DEFAULT NULL COMMENT '营销类型；0无营销、1拼团营销',
    `market_deduction_amount` decimal(8, 2)                                                  NULL     DEFAULT NULL COMMENT '营销金额；优惠金额',
    `pay_amount`              decimal(8, 2)                                                  NOT NULL COMMENT '支付金额',
    `create_time`             datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`             datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uq_order_id` (`order_id` ASC) USING BTREE,
    INDEX `idx_user_id_product_id` (`user_id` ASC, `product_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_order
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
