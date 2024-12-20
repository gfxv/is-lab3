package dev.gfxv.lab3.utils;

public class JwtParser {
    public static String parseTokenFromHeader(String header) {
        return header.split(" ")[1];
    }
}
