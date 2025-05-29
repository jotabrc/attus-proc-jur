package attus.proc.proc_jur.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class TokenConfig {

    @Value("${jwt.config.prefix}")
    public String PREFIX;

    @Value("${jwt.config.key}")
    public String KEY;

    @Value("${jwt.config.expiration}")
    public Long EXPIRATION;
}
