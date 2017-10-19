package com.git.lee.io.aio.metrics.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 11:10
 */
public class ApiUtil {

    private final static String UTF8_ENCODING = "UTF-8";
    private final static Charset UTF8_CHARSET = Charset.forName(UTF8_ENCODING);

    public static void writeShortString(ByteBuffer buffer, String s) {
        if (s == null) {
            buffer.putShort((short)0);
        } else {
            byte[] data = toBytes(s);
            buffer.putShort((short)data.length);
            buffer.put(data);
        }
    }

    public static String readShortString(ByteBuffer buffer) {
        short size = buffer.getShort();
        if (size == 0) {
            return null;
        }
        byte[] data = new byte[size];
        buffer.get(data);
        return new String(data, UTF8_CHARSET);
    }

    public static int shortStringLength(String s) {
        if (s == null) return 2;
        return 2 + toBytes(s).length;
    }

    private static byte[] toBytes(String s) {
        return s.getBytes(UTF8_CHARSET);
    }
}
