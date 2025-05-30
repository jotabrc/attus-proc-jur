package unsecure;

import attus.proc.proc_jur.config.jwt.TokenCreator;
import attus.proc.proc_jur.config.jwt.TokenObject;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * For test only
 */
public class Unsecure {

    public static void main(String[] args) {

        String key = "ezBqr2G/W2c5L6d8rh8Gmfaweu84043Y/91IvSYURluxvFz6vxDxM6IwEaxteZU46zhwKWzI65oK541WdWPs8g=="; //getKey();
        var obj = TokenObject.builder()
                .subject("admin-uuid")
                .issuedAt(Date.from(Instant.now()))
                .expiration(new Date(System.currentTimeMillis() + 36000000000L))
                .roles(List.of("ADMIN"))
                .build();
        String jwt = TokenCreator.encode(
                "Bearer",
                key,
                obj);
        System.out.println("key:" + key);
        System.out.println(jwt);
    }

    public static String getKey() {
        Key key = Jwts.SIG.HS512.key().build();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
