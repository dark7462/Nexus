package com.dark.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtProviderTests {

    @Test
    void shouldGenerateTokenAndReadEmailClaim() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "secret");

        String token = JwtProvider.generateToken(authentication);

        assertNotNull(token);
        String extractedEmail = JwtProvider.getEmailFromJwtToken("Bearer " + token);
        assertEquals("user@example.com", extractedEmail);
    }
}
