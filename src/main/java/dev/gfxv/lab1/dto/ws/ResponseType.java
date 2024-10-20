package dev.gfxv.lab1.dto.ws;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ResponseType {
    GET,
    ADD,
    UPDATE,
    DELETE,
}
