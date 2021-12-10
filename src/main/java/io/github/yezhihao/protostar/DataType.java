package io.github.yezhihao.protostar;

/**
 * 支持的数据类型
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public enum DataType {

    /** 无符号单字节整型(8位) */
    BYTE(1),
    /** 无符号双字节整型(16位) */
    WORD(2),
    /** 无符号四字节整型(32位) */
    DWORD(4),
    /** 无符号八字节整型(64位) */
    QWORD(8),
    /** 字节数组 */
    BYTES(-1),
    /** BCD8421码 */
    BCD8421(-1),
    /** 十六进制字符串 */
    HEX(-1),
    /** 字符串 */
    STRING(-1),
    /** 对象 */
    OBJ(-1),
    /** 列表 */
    LIST(-1),
    /** 字典 */
    MAP(-1);

    public int length;

    DataType(int length) {
        this.length = length;
    }
}