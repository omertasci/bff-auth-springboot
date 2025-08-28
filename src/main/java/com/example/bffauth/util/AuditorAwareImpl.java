package com.example.bffauth.util;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        // Here you can get logged-in user from SecurityContext
        return Optional.of("system"); // fallback if no user is logged in
    }
}

