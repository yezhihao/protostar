package io.github.yezhihao.protostar.convert;

import io.github.yezhihao.protostar.annotation.Field;

import java.util.Map;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class T0200 {

    @Field(length = 2, desc = "消息ID")
    protected int messageId;
    @Field(length = 2, desc = "消息体属性")
    protected int properties;
    @Field(length = 1, desc = "协议版本号", version = 1)
    protected int protocolVersion;
    @Field(length = 6, charset = "BCD", desc = "终端手机号", version = {-1, 0})
    @Field(length = 10, charset = "BCD", desc = "终端手机号", version = 1)
    protected String clientId;
    @Field(length = 2, desc = "流水号", version = {-1, 0})
    @Field(length = 2, desc = "流水号", version = 1)
    protected int serialNo;
    @Field(length = 2, desc = "消息包总数", version = {-1, 0})
    @Field(length = 2, desc = "消息包总数", version = 1)
    protected Integer packageTotal;
    @Field(length = 2, desc = "包序号", version = {-1, 0})
    @Field(length = 2, desc = "包序号", version = 1)
    protected Integer packageNo;
    @Field(length = 4, desc = "报警标志")
    private int warnBit;
    @Field(length = 4, desc = "状态")
    private int statusBit;
    @Field(length = 4, desc = "纬度")
    private int latitude;
    @Field(length = 4, desc = "经度")
    private int longitude;
    @Field(length = 2, desc = "高程(米)")
    private int altitude;
    @Field(length = 2, desc = "速度(1/10公里每小时)")
    private int speed;
    @Field(length = 2, desc = "方向")
    private int direction;
    @Field(length = 6, charset = "BCD", desc = "时间(YYMMDDHHMMSS)")
    private String dateTime;
    @Field(index = 4, desc = "属性", version = 0, converter = AttributeSchema.class)
    @Field(index = 4, desc = "属性", version = 1, converter = AttributeSchemaV2.class)
    private Map<Integer, Object> attributes;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getProperties() {
        return properties;
    }

    public void setProperties(int properties) {
        this.properties = properties;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getPackageTotal() {
        return packageTotal;
    }

    public void setPackageTotal(Integer packageTotal) {
        this.packageTotal = packageTotal;
    }

    public Integer getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(Integer packageNo) {
        this.packageNo = packageNo;
    }

    public int getWarnBit() {
        return warnBit;
    }

    public void setWarnBit(int warnBit) {
        this.warnBit = warnBit;
    }

    public int getStatusBit() {
        return statusBit;
    }

    public void setStatusBit(int statusBit) {
        this.statusBit = statusBit;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Map<Integer, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Integer, Object> attributes) {
        this.attributes = attributes;
    }
}