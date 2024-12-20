package dev.gfxv.lab3.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gfxv.lab3.dto.SpaceMarineDTO;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.List;

public class YmlParser {

    public static List<SpaceMarineDTO> parseFile(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        return objectMapper.readValue(file.getInputStream(), new TypeReference<List<SpaceMarineDTO>>() {});
    }
}
