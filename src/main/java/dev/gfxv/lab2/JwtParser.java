package dev.gfxv.lab2;

public class JwtParser {
    public static String parseTokenFromHeader(String header) {
        return header.split(" ")[1];
    }
}
