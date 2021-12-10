package io.github.yezhihao.protostar.util;

import io.netty.buffer.ByteBuf;

/**
 * Netty ByteBuf工具类
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class ByteBufUtils {

    public static int readInt(ByteBuf input, int length) {
        int value;
        switch (length) {
            case 1:
                value = input.readUnsignedByte();
                break;
            case 2:
                value = input.readUnsignedShort();
                break;
            case 3:
                value = input.readUnsignedMedium();
                break;
            case 4:
                value = input.readInt();
                break;
            default:
                throw new RuntimeException("unsupported length: " + length + " (expected: 1, 2, 3, 4)");
        }
        return value;
    }

    public static void writeInt(ByteBuf output, int length, int value) {
        switch (length) {
            case 1:
                output.writeByte(value);
                break;
            case 2:
                output.writeShort(value);
                break;
            case 3:
                output.writeMedium(value);
                break;
            case 4:
                output.writeInt(value);
                break;
            default:
                throw new RuntimeException("unsupported length: " + length + " (expected: 1, 2, 3, 4)");
        }
    }

    public static int getInt(ByteBuf input, int index, int length) {
        int value;
        switch (length) {
            case 1:
                value = input.getUnsignedByte(index);
                break;
            case 2:
                value = input.getUnsignedShort(index);
                break;
            case 3:
                value = input.getUnsignedMedium(index);
                break;
            case 4:
                value = input.getInt(index);
                break;
            default:
                throw new RuntimeException("unsupported length: " + length + " (expected: 1, 2, 3, 4)");
        }
        return value;
    }

    public static void setInt(ByteBuf output, int index, int length, int value) {
        switch (length) {
            case 1:
                output.setByte(index, value);
                break;
            case 2:
                output.setShort(index, value);
                break;
            case 3:
                output.setMedium(index, value);
                break;
            case 4:
                output.setInt(index, value);
                break;
            default:
                throw new RuntimeException("unsupported length: " + length + " (expected: 1, 2, 3, 4)");
        }
    }

    public static void writeFixedLength(ByteBuf output, int length, byte[] bytes) {
        int srcPos = length - bytes.length;
        if (srcPos > 0) {
            output.writeBytes(bytes);
            output.writeBytes(new byte[srcPos]);
        } else if (srcPos < 0) {
            output.writeBytes(bytes, -srcPos, length);
        } else {
            output.writeBytes(bytes);
        }
    }

    public static boolean startsWith(ByteBuf haystack, byte[] prefix) {
        for (int i = 0, j = haystack.readerIndex(); i < prefix.length; )
            if (prefix[i++] != haystack.getByte(j++))
                return false;
        return true;
    }
}