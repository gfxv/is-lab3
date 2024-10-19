package dev.gfxv.lab1.dto.ws;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableRecordsRequest {
    int page;
    int size;
}
