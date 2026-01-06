package com.interview.j2ee.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/hash-password")
    public String hashPassword(@RequestParam String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        System.out.println("Plain password: " + password);
        System.out.println("BCrypt hash: " + hash);
        System.out.println("Testing match: " + encoder.matches(password, hash));
        return "Hash: " + hash;
    }
    
    @GetMapping("/test-hash")
    public String testHash(@RequestParam String password, @RequestParam String hash) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(password, hash);
        return "Password: " + password + "\nHash: " + hash + "\nMatches: " + matches;
    }
}
