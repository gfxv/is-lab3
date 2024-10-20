package dev.gfxv.lab1.controller;

import dev.gfxv.lab1.JwtParser;
import dev.gfxv.lab1.dto.AdminRequestDTO;
import dev.gfxv.lab1.dto.UserDTO;
import dev.gfxv.lab1.security.JwtProvider;
import dev.gfxv.lab1.service.AdminService;
import dev.gfxv.lab1.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {

    String adminRole = "ADMIN";

    UserService userService;
    AdminService adminService;

    JwtProvider jwtProvider;

    @Autowired
    public AdminController(
        UserService userService,
        AdminService adminService,
        JwtProvider jwtProvider
    ) {
        this.userService = userService;
        this.adminService = adminService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/new")
    public ResponseEntity<String> request(
        @RequestHeader("Authorization") String tokenHeader
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);


        adminService.newAdminRequest(username);
        return new ResponseEntity<>("Request successfully sent", HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<?> pending(
        @RequestHeader("Authorization") String tokenHeader
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);
        if (!userService.userHasRole(username, adminRole)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<AdminRequestDTO> requests =  adminService.getPendingRequests();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PostMapping("/accept")
    public ResponseEntity<String> accept(
        @RequestHeader("Authorization") String tokenHeader,
        @RequestBody() AdminRequestDTO request
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);
        if (!userService.userHasRole(username, adminRole)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            adminService.acceptRequest(request.getId());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deny")
    public ResponseEntity<String> deny(
        @RequestHeader("Authorization") String tokenHeader,
        @RequestBody() AdminRequestDTO request
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);
        if (!userService.userHasRole(username, adminRole)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            adminService.denyRequest(request.getId());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/is-admin")
    public ResponseEntity<?> isAdmin(
        @RequestHeader("Authorization") String tokenHeader
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);
        return new ResponseEntity<>(userService.userHasRole(username, adminRole), HttpStatus.OK);
    }

    @GetMapping("/is-pending")
    public ResponseEntity<?> isPending(
            @RequestHeader("Authorization") String tokenHeader
    ) {
        String token = JwtParser.parseTokenFromHeader(tokenHeader);
        String username = jwtProvider.getUsernameFromJwt(token);
        return new ResponseEntity<>(adminService.isPendingUser(username), HttpStatus.OK);
    }
}
