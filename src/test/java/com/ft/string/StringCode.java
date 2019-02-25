package com.ft.string;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class StringCode {

    @Test
    public void test() throws UnsupportedEncodingException {
        // win7默认字符编码格式为gb2312
        // java string编码格式为unicode (utf-16 le)，不带前缀
        // 从文件(假设是GB2312编码)或其他地方载入到string经历JVM的字节码转换 文件编码(内存数据) -> UNICODE，这里是 GB2312 -> UNICODE
        // 从文件(假设是UTF8编码)或其他地方载入到string经历JVM的字节码转换 文件编码(内存数据) -> UNICODE，这里是 UTF8 -> UNICODE
        // 从String打印字符串，JVM会把 UNICODE 转成 系统默认字符串，这里是 UNICODE -> GB2312

        // 你好啊，编码格式
        // UNICODE  4F 60 59 7D 55 4A
        // GB2312   C4 E3 BA C3 B0 A1
        // UTF8     E4 BD A0 E5 A5 BD E5 95 8A

        // 0 = '你' 20320 = 4F60
        // 1 = '好' 22909 = 597D
        // 2 = '啊' 21834 = 554A
        String test = "你好啊";
        System.out.println(test);

        byte[] btUtf8   = test.getBytes();  // 该文件编码格式为 utf-8格式
        byte[] btUtf82  = test.getBytes("utf-8");
        byte[] btUnicode= test.getBytes("unicode");
        byte[] btGbk    = test.getBytes("gbk");

        displayByte(btUtf8, "utf-8");
        displayByte(btUtf82, "utf-8");
        displayByte(btUnicode, "unicode");
        displayByte(btGbk, "gbk");

        conv(test, "utf-8");
        conv(test, "unicode");
        conv(test, "gbk");
    }

    private void displayByte(byte[] bt, String code) {
        System.out.print("encode "+code+" bt len is " + bt.length + ", data is ");
        for (byte b : bt) {
            System.out.printf("%02x ", b);
        }
        System.out.println("");
    }

    private void conv(String src, String code) throws UnsupportedEncodingException {
        String dst = new String(src.getBytes(code), code);
        System.out.println("src is "+ src + " convert to " + code + ", dst is "+dst);
    }
}
