package servicos.gratitude.be_gratitude_capacita.JWTImplement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GerenciadorTokenJwt {

    private String secret = "RXhpc3RlIHVtYSB0ZW9yaWEgcXVlIGRpeiBxdWUsIHNlIHVtIGRpYSBhbGd16W0gZGVzY29icmlyIGV4YXRhbWVudGUgcGFyYSBxdWUgc2VydmUgbyBVbml2ZXJzbyBlIHBvciBxdWUgZWxlIGVzdOEgYXF1aSwgZWxlIGRlc2FwYXJlY2Vy4SBpbnN0YW50YW5lYW1lbnRlIGUgc2Vy4SBzdWJzdGl0de1kbyBwb3IgYWxnbyBhaW5kYSBtYWlzIGVzdHJhbmhvIGUgaW5leHBsaWPhdmVsLiBFeGlzdGUgdW1hIHNlZ3VuZGEgdGVvcmlhIHF1ZSBkaXogcXVlIGlzc28gauEgYWNvbnRlY2V1Li4u";
    private long validity = 3600000;

    public String getUsernameFromToken(String token) {
        return getClaimForToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimForToken(token, Claims::getExpiration);
    }

    public String generateToken(final Authentication authentication) {
        final String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(";"));

        return Jwts.builder().setSubject(authentication.getName())
                .signWith(parseSecret()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity * 1_000)).compact();
    }

    public <T> T getClaimForToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(parseSecret())
                .build()
                .parseClaimsJws(token).getBody();
    }

    private SecretKey parseSecret() {
        return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }
}
