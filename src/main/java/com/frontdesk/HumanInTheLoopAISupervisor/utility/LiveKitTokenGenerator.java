package com.frontdesk.HumanInTheLoopAISupervisor.utility;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LiveKitTokenGenerator {

    private static final String API_KEY = "APIon6hzEXUTsy9";
    private static final String API_SECRET = "TkCbWCFhbjAlC7xiEIvNY4I6OlRZMJ6PvzvGgNFafSD";
    private static final String LIVEKIT_URL = "wss://humanintheloopaisupervisor-ca57ux4l.livekit.cloud";

    public static String generateToken(String username) {
        long now = System.currentTimeMillis();
        Date expiration = new Date(now + 3600000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("identity", username);
        claims.put("room", "support-room");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(API_KEY)
                .setIssuedAt(new Date(now))
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, API_SECRET.getBytes())
                .compact();
    }

    public static void main(String[] args) {
        String token = generateToken("supervisor1");
        System.out.println("Generated Token: " + token);
    }
}

