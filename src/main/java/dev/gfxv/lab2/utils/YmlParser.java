package dev.gfxv.lab2.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gfxv.lab2.dto.SpaceMarineDTO;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YmlParser {

    public static List<SpaceMarineDTO> parseFile(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        return objectMapper.readValue(file.getInputStream(), new TypeReference<List<SpaceMarineDTO>>() {});
    }
}
