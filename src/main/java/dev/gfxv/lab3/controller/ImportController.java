package dev.gfxv.lab3.controller;

import dev.gfxv.lab3.dto.SpaceMarineDTO;
import dev.gfxv.lab3.security.JwtProvider;
import dev.gfxv.lab3.service.ImportService;
import dev.gfxv.lab3.service.UserService;
import dev.gfxv.lab3.utils.JwtParser;
import dev.gfxv.lab3.utils.YmlParser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/api/import")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImportController {

    ImportService importService;
    UserService userService;
    JwtProvider jwtProvider;

    @Autowired
    public ImportController(
        ImportService importService,
        UserService userService,
        JwtProvider jwtProvider
    ) {
        this.importService = importService;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/history")
    public ResponseEntity<?> getImports(
            @RequestHeader("Authorization") String tokenHeader
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);

        if (userService.isAdmin(username)) {
            return new ResponseEntity<>(importService.getALLImportHistory(), HttpStatus.OK);
        }
        return new ResponseEntity<>(importService.getImportHistoryByUser(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> importFile(
        @RequestHeader("Authorization") String tokenHeader,
        @RequestParam("file") MultipartFile file
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);

        if (file.isEmpty()) {
            return new ResponseEntity<>("Empty file", HttpStatus.BAD_REQUEST);
        }
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".yml")) {
            return new ResponseEntity<>("Provided file is not yml", HttpStatus.BAD_REQUEST);
        }

        try {
            List<SpaceMarineDTO> marines = YmlParser.parseFile(file);
            System.out.println("Marines parsed: " + marines);
            importService.importMarines(marines, username);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
