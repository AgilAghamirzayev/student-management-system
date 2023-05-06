package com.mastercode.studentmanagementback.security.jwt;

import static java.time.temporal.ChronoUnit.DAYS;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class JWTUtil {

  private static final String SECRET_KEY =
      "test_27e872y872t2t7tgvcgsvd_n8br783873-qheui-ehuihuihuehuyg63363tc-ekjfiuh7";

  public String issueToken(String subject) {
    return issueToken(subject, Map.of());
  }

  public String issueToken(String subject, String... scopes) {
    return issueToken(subject, Map.of("scopes", scopes));
  }

  public String issueToken(String subject, List<String> scopes) {
    return issueToken(subject, Map.of("scopes", scopes));
  }

  public String issueToken(String subject, Map<String, Object> claims) {
    return Jwts
        .builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuer("mastercode.az")
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(
            Date.from(
                Instant.now().plus(15, DAYS)
            )
        )
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String getSubject(String token) {
    return getClaims(token).getSubject();
  }

  private Claims getClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }

  public boolean isTokenValid(String jwt, String username) {
    String subject = getSubject(jwt);
    return subject.equals(username) && !isTokenExpired(jwt);
  }

  private boolean isTokenExpired(String jwt) {
    Date today = Date.from(Instant.now());
    return getClaims(jwt).getExpiration().before(today);
  }
}
