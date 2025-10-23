package com.frontdesk.HumanInTheLoopAISupervisor.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/livekit")
@CrossOrigin("*")
public class LiveKitController {

    private static final String API_KEY = "APIon6hzEXUTsy9"; // your key
    private static final String API_SECRET = "TkCbWCFhbjAlC7xiEIvNY4I6OlRZMJ6PvzvGgNFafSD"; // your secret

    @GetMapping("/token")
    public Map<String, String> getToken(@RequestParam String identity, @RequestParam String room) {

        long now = System.currentTimeMillis() / 1000;
        long exp = now + 3600;  // 1 hour

        String token = Jwts.builder()
                .setHeaderParam("kid", API_KEY)
                .setIssuer(API_KEY)
                .setSubject(identity)
                .setExpiration(new Date(exp * 1000))
                .setIssuedAt(new Date(now * 1000))
                .claim("video", Map.of(
                        "roomJoin", true,
                        "room", room,
                        "canPublish", true,
                        "canSubscribe", true
                ))
                .signWith(SignatureAlgorithm.HS256, API_SECRET.getBytes())
                .compact();

        return Map.of("token", token);
    }
}
