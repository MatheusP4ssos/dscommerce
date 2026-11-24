package com.teste.dscommerce.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

class JwtConfigTest {

  private final JwtConfig config = new JwtConfig();

  @Test
  void ephemeralSecretChangesOnEveryCall() {
    SecretKey first = config.jwtSecretKey("", true);
    SecretKey second = config.jwtSecretKey("ignored", true);

    assertEquals("HmacSHA256", first.getAlgorithm());
    assertEquals(32, first.getEncoded().length);
    assertFalse(Arrays.equals(first.getEncoded(), second.getEncoded()));
  }

  @Test
  void nonEphemeralSecretIsRequiredAndMustBeStrong() {
    assertThrows(IllegalStateException.class, () -> config.jwtSecretKey("", false));
    assertThrows(IllegalStateException.class, () -> config.jwtSecretKey("curta", false));
  }

  @Test
  void nonEphemeralSecretIsDecodedFromBase64() {
    String secret = Base64.getEncoder().encodeToString(
        "0123456789abcdef0123456789abcdef".getBytes(StandardCharsets.UTF_8));

    SecretKey key = config.jwtSecretKey(secret, false);

    assertEquals("HmacSHA256", key.getAlgorithm());
    assertEquals(32, key.getEncoded().length);
  }
}
