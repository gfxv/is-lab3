package dev.gfxv.lab1.dto.auth;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponseDTO {

    final String token;
    final String tokenType = "Bearer ";

    public AuthResponseDTO(String token) {
        this.token = token;
    }

}

