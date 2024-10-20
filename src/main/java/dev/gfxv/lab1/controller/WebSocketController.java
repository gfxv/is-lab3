package dev.gfxv.lab1.controller;

import dev.gfxv.lab1.dto.SpaceMarineDTO;
import dev.gfxv.lab1.dto.ws.ResponseType;
import dev.gfxv.lab1.dto.ws.TableRecordsRequest;
import dev.gfxv.lab1.dto.ws.TableRecordsResponse;
import dev.gfxv.lab1.service.SpaceMarineService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collections;
import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketController {

    SpaceMarineService spaceMarineService;

    int maxPageSize = 10;

    @Autowired
    public WebSocketController(SpaceMarineService spaceMarineService) {
        this.spaceMarineService = spaceMarineService;
    }

    @MessageMapping("/marines")
    @SendTo("/records/changes")
    public TableRecordsResponse sendMarines(
        @Payload TableRecordsRequest request
    ) {
        if (request.getPage() > maxPageSize) {
            return TableRecordsResponse.builder()
                    .records(Collections.emptyList())
                    .error(String.format("Maximum page size is %d", maxPageSize))
                    .build();
        }

        List<SpaceMarineDTO> marines = spaceMarineService.getAllMarinesAsPage(request.getPage(), request.getSize());
        return TableRecordsResponse.builder()
                .type(ResponseType.GET)
                .records(marines)
                .build();
    }

}
