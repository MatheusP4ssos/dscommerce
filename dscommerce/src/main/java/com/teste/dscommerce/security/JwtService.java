package com.teste.dscommerce.security;

import com.teste.dscommerce.dto.TokenDTO;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final JwtEncoder jwtEncoder;
  private final long duration;
  private final String issuer;

  public JwtService(JwtEncoder jwtEncoder,
      @Value("${security.jwt.duration}") long duration,
      @Value("${security.jwt.issuer}") String issuer) {
    this.jwtEncoder = jwtEncoder;
    this.duration = duration;
    this.issuer = issuer;
  }

  public TokenDTO generateToken(Authentication authentication) {
    Instant now = Instant.now();
    Instant expiresAt = now.plusSeconds(duration);

    List<String> roles = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer(issuer)
        .issuedAt(now)
        .expiresAt(expiresAt)
        .subject(authentication.getName())
        .claim("roles", roles)
        .build();

    JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
    String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

    return new TokenDTO(token, "Bearer", duration);
  }
}
