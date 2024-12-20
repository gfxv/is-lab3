package dev.gfxv.lab3.dto.ws;

import dev.gfxv.lab3.dto.SpaceMarineDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableRecordsResponse {
    ResponseType type;
    List<SpaceMarineDTO> records;
    String error; // ?
}
