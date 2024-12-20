package dev.gfxv.lab3.controller;

import dev.gfxv.lab3.security.JwtProvider;
import dev.gfxv.lab3.service.ImportService;
import dev.gfxv.lab3.service.StorageService;
import dev.gfxv.lab3.service.UserService;
import dev.gfxv.lab3.utils.JwtParser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
    StorageService storageService;

    @Autowired
    public ImportController(
        ImportService importService,
        UserService userService,
        JwtProvider jwtProvider,
        StorageService storageService
    ) {
        this.importService = importService;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.storageService = storageService;
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
            importService.importMarinesFromFile(file, username);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(
        @PathVariable String fileName
    ) {
        try {
            InputStream fileStream = storageService.downloadFile(fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(fileStream));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
