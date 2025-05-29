package attus.proc.proc_jur.security;

import attus.proc.proc_jur.config.jwt.TokenConfig;
import attus.proc.proc_jur.config.jwt.TokenCreator;
import attus.proc.proc_jur.config.jwt.TokenObject;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenGlobalFilter extends OncePerRequestFilter {

    private final TokenConfig tokenConfig;

    @Autowired
    public TokenGlobalFilter(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        try {
            if (token != null && !token.isEmpty()) {
                token = token.substring(7).trim();
                TokenObject tokenObject = TokenCreator.decode(token, tokenConfig.PREFIX, tokenConfig.getKEY());
                List<SimpleGrantedAuthority> authorities = this.authorities(tokenObject.getRoles());
                UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(tokenObject.getSubject(), (Object)null, authorities);
                SecurityContextHolder.getContext().setAuthentication(userToken);
            } else {
                SecurityContextHolder.clearContext();
            }

            filterChain.doFilter(request, response);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | ExpiredJwtException var10) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }

    private List<SimpleGrantedAuthority> authorities(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors
                        .toList());
    }
}
