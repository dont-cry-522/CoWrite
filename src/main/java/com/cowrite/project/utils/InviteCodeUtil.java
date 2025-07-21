package com.cowrite.project.utils;

public class InviteCodeUtil {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(long id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.insert(0, BASE62.charAt((int) (id % 62)));
            id /= 62;
        }
        return sb.toString();
    }

    public static long decode(String shortCode) {
        long result = 0;
        for (char c : shortCode.toCharArray()) {
            result *= 62;
            if ('0' <= c && c <= '9') result += c - '0';
            else if ('a' <= c && c <= 'z') result += c - 'a' + 10;
            else if ('A' <= c && c <= 'Z') result += c - 'A' + 36;
        }
        return result;
    }

    public static void main(String[] args) {
        long longs = SnowflakeIdGenerator.nextId();
        System.out.println(longs);
        System.out.println(encode(longs));
        System.out.println(decode(encode(longs)));
    }
}