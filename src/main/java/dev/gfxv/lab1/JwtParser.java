package dev.gfxv.lab1;

public class JwtParser {
    public static String parseTokenFromHeader(String header) {
        return header.split(" ")[1];
    }
}
