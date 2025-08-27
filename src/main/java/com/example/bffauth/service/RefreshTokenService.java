package com.example.bffauth.service;

import com.example.bffauth.model.RefreshToken;
import com.example.bffauth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repo;

    public RefreshTokenService(RefreshTokenRepository repo) {
        this.repo = repo;
    }

    public RefreshToken create(String username, String domain, String deviceId, long ttlMs) {
        String plain = UUID.randomUUID().toString() + "-" + UUID.randomUUID();
        String hash = sha256(plain);

        RefreshToken rt = new RefreshToken();
        rt.setUsername(username);
        rt.setDomain(domain);
        rt.setDeviceId(deviceId);
        rt.setTokenHash(hash);
        rt.setExpiresAt(Instant.now().plusMillis(ttlMs));
        rt.setFamilyId(UUID.randomUUID().toString());
        repo.save(rt);

        // plain token returned to client
        rt.setTokenHash(plain); // temporarily reuse field to return plain (caller should treat carefully)
        return rt;
    }

    public Optional<RefreshToken> findByPlain(String plain) {
        String hash = sha256(plain);
        return repo.findByTokenHash(hash);
    }

    public RefreshToken rotate(RefreshToken old, long ttlMs) {
        // revoke old
        old.setRevoked(true);
        repo.save(old);

        // create new with same familyId
        String plain = UUID.randomUUID().toString() + "-" + UUID.randomUUID();
        String hash = sha256(plain);

        RefreshToken rt = new RefreshToken();
        rt.setUsername(old.getUsername());
        rt.setDomain(old.getDomain());
        rt.setDeviceId(old.getDeviceId());
        rt.setTokenHash(hash);
        rt.setExpiresAt(Instant.now().plusMillis(ttlMs));
        rt.setFamilyId(old.getFamilyId());
        repo.save(rt);

        rt.setTokenHash(plain); // return plain in object
        return rt;
    }

    public void revokeFamily(String familyId) {
        repo.findAll().stream()
                .filter(t -> familyId.equals(t.getFamilyId()))
                .forEach(t -> { t.setRevoked(true); repo.save(t); });
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
