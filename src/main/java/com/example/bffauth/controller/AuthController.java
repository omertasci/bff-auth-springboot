package com.example.bffauth.controller;

import com.example.bffauth.entity.RefreshToken;
import com.example.bffauth.service.RefreshTokenService;
import com.example.bffauth.util.JwtUtil;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final JwtUtil jwtUtil;

  private final RefreshTokenService refreshTokenService;

  @Value("${app.jwt.refresh-ms}")
  private long refreshMs;

  public AuthController(JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
    this.jwtUtil = jwtUtil;
    this.refreshTokenService = refreshTokenService;
  }

  // Basit örnek: gerçek projede user doğrulama DB/LDAP/3rd-party yapılmalı
  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestParam String username,
      @RequestParam String password,
      @RequestParam String domain,
      @RequestParam(required = false) String deviceId) {

    if (!"user".equals(username) || !"pass".equals(password)) {
      return ResponseEntity.status(401).body("Invalid credentials");
    }

    String access = jwtUtil.generateAccessToken(username, domain);
    RefreshToken rt =
        refreshTokenService.create(
            username, domain, deviceId == null ? "unknown" : deviceId, refreshMs);
    String refreshPlain = rt.getTokenHash(); // metodumuz plain token'ı buraya koydu

    return ResponseEntity.ok(Map.of("accessToken", access, "refreshToken", refreshPlain));
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@RequestParam String refreshToken) {
    var maybe = refreshTokenService.findByPlain(refreshToken);
    if (maybe.isEmpty()) {
      return ResponseEntity.status(401).body("Invalid refresh token");
    }
    RefreshToken stored = maybe.get();

    if (stored.isRevoked() || stored.getExpiresAt().isBefore(java.time.Instant.now())) {
      // rotate-while-reuse veya expired => family revoke
      refreshTokenService.revokeFamily(stored.getFamilyId());
      return ResponseEntity.status(401).body("Refresh token invalid/expired");
    }

    // rotation
    RefreshToken newRt = refreshTokenService.rotate(stored, refreshMs);
    String newRefreshPlain = newRt.getTokenHash();

    String newAccess = jwtUtil.generateAccessToken(stored.getUsername(), stored.getDomain());

    return ResponseEntity.ok(Map.of("accessToken", newAccess, "refreshToken", newRefreshPlain));
  }
}
