package pers.zcc.scm.common.constant;

import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * 常量类
 * 
 * @author zhangchangchun
 * @since 2021年4月1日
 */
public interface Constants {

    /**
     * 批处理权限编码
     */
    String PRIVILEGE_INSERT_CODE = "insert";

    /**
     * 批处理权限描述
     */
    String PRIVILEGE_INSERT_DESC = "新增";

    /**
     * 批处理权限编码
     */
    String PRIVILEGE_UPDATE_CODE = "update";

    /**
     * 批处理权限描述
     */
    String PRIVILEGE_UPDATE_DESC = "修改";

    /**
     * 批处理权限编码
     */
    String PRIVILEGE_DELETE_CODE = "delete";

    /**
     * 批处理权限描述
     */
    String PRIVILEGE_DELETE_DESC = "删除";

    /**
     * 批处理权限编码
     */
    String PRIVILEGE_QUERY_CODE = "select";

    /**
     * 批处理权限描述
     */
    String PRIVILEGE_QUERY_DESC = "查询";

    /**
     * 批处理权限编码
     */
    String PRIVILEGE_BATCH_CODE = "batch";

    /**
     * 批处理权限描述
     */
    String PRIVILEGE_BATCH_DESC = "批量保存";

    /**
     * 导出权限编码
     */
    String PRIVILEGE_EXPORT_CODE = "export";

    /**
     * 导出权限描述
     */
    String PRIVILEGE_EXPORT_DESC = "导出";

    String CLOUD_API_SUCCESS_CODE_000 = "\"code\":\"000\"";

    /**
     * true 十六进制表示 55H
     */
    int TRUE_HEX_0X55 = 0x55;

    /**
     * false 十六进制表示 AAH
     */
    int FALSE_HEX_0XAA = 0xAA;

    /**
     * false的字符值‘0’
     */
    char BOOLEAN_FALSE_CHAR_0 = '0';

    /**
     * true的字符值‘1’
     */
    char BOOLEAN_TRUE_CHAR_1 = '1';

    /**
     * true十六进制值 55H的二进制字符串
     */
    String BOOLEAN_TRUE_55H = "0000000001010101";

    /**
     * 取车写入的停车位（当前流程绑定的停车位）信息存放地址起始偏移量
     */
    int PICKUP_SPACE_OFFSET = 0x00;

    /**
     * 存车分配的停车位（当前流程绑定的停车位）信息存放地址起始偏移量
     */
    int PARKING_SPACE_OFFSET = 0x26;

    /**
     * 取车的车牌号起始位置偏移量
     */
    int PICKUP_PLATE_NUM_OFFSET = 0x01;

    /**
     * 存车的车牌号起始位置偏移量
     */
    int PARKING_PLATE_NUM_OFFSET = 0x28;

    /**
     * 存车启动信号偏移量
     */
    int PARK_CAR_PROCESS_TRIGGER_OFFSET = 0x06;

    /**
     * 取车启动信号偏移量
     */
    int PICK_UP_PROCESS_TRIGGER_OFFSET = 0x07;

    /**
     * 存车流程步骤字信号（低->高 0-5位）起始偏移量
     */
    int PARK_CAR_PROCESS_WORD_OFFSET = 0x08;

    /**
     * 取车流程步骤字信号（低->高 0-5位）起始偏移量
     */
    int PICK_UP_PROCESS_WORD_OFFSET = 0x09;

    /**
     * 车库存车流程开始信号在状态字中的bit位
     */
    int PARKING_START_BIT_OFFSET = 6;

    /**
     * 车库取车流程开始信号在状态字中bit位
     */
    int PICK_UP_BEGIN_BIT_OFFSET = 7;

    /**
     * 车库取车流程结束信号在状态字中bit位
     */
    int PICK_UP_END_BIT_OFFSET = 9;

    /**
     * 电控系统关键状态标志字起始地址
     */
    int STATUS_WORD_OFFSET = 0x0A;

    /**
     * 存车前车辆停放到位信号在状态字中bit位
     */
    int PARKING_READY_BIT_OFFSET = 1;

    /**
     * 当前车库所有车位(至多8字)是否空闲状态量的起始偏移量
     */
    int ALL_SPACE_ISAVAILABLE_OFFSET_START_INDEX = 0X0C;

    /**
     * 当前车库所有车位(至多8字)是否检修中状态量的起始偏移量
     */
    int ALL_SPACE_ISNOTWORKING_OFFSET_START_INDEX = 0X14;

    /**
     * 当前车库所有车位(至多8字)是否被预订状态量的起始偏移量
     */
    int ALL_SPACE_ISBOOKED_OFFSET_START_INDEX = 0X1C;

    /**
     * 是否大车位位信号在状态字中的偏移量
     */
    int IS_BIGGER_BIT_OFFSET = 2;

    /**
     * 车库门开闭状态在状态字中的偏移量
     */
    int IS_CARBARN_GATE_OPENNING_BIT_OFFSET = 8;

    /**
     * 车牌和车位读写允许和事务完成锁标志偏移量
     */
    int PLATE_NUM_AND_SPACE_CODE_RW_LOCK__OFFSET = 0x24;

    /**
    * 取车成功通知队列读写锁地址偏移量
    */
    int PICK_RESULT_NOTIFY_QUEUE_RW_LOCK_OFFSET = 0x25;

    /**
     * 取车成功通知队列起始地址偏移量
     */
    int PICK_RESULT_NOTIFY_QUEUE_OFFSET = 0x2D;

    /**
     * 存车成功通知队列读写锁地址偏移量
     */
    int PARK_RESULT_NOTIFY_QUEUE_RW_LOCK_OFFSET = 0x26;

    /**
     * 存车成功通知队列起始地址偏移量
     */
    int PARK_RESULT_NOTIFY_QUEUE_OFFSET = 0x46;

    /**
     * 中国时区TimeZone 
     * 可选方法toZoneId
     */
    TimeZone TIME_ZONE_CHINA = TimeZone.getTimeZone("GMT+08:00");

    /**
     * 中国时区ZoneOffset
     */
    ZoneOffset ZONE_OFFSET_CHINA = ZoneOffset.of("+08:00");

}
