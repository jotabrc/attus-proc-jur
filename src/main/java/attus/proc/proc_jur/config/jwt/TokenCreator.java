package attus.proc.proc_jur.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.List;
import java.util.stream.Collectors;

public class TokenCreator {

    public static String encode(String prefix, String key, TokenObject tokenObject) {

        SecretKey signingKey = getSecretKey(key);

        String token = Jwts.builder()
                .subject(tokenObject.getSubject())
                .issuedAt(tokenObject.getIssuedAt())
                .expiration(tokenObject.getExpiration())
                .claim("Authorization", checkRoles(tokenObject.getRoles()))
                .signWith(signingKey)
                .compact();
        return prefix + " " + token;
    }

    public static TokenObject decode(String token, String prefix, String key)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {

        SecretKey signingKey = getSecretKey(key);

        token = token.replace(prefix, "");

        var claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return TokenObject
                .builder()
                .subject(claims.getSubject())
                .expiration(claims.getExpiration())
                .issuedAt(claims.getIssuedAt())
                .roles((List<String>) claims.get("Authorization"))
                .build();

    }

    private static List<String> checkRoles(List<String> roles) {
        return roles.stream().map(s -> "ROLE_".concat(s.replaceAll("ROLE_",""))).collect(Collectors.toList());
    }

    private static SecretKey getSecretKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "HmacSHA512");
    }
}
