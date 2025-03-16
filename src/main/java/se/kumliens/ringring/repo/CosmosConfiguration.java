package se.kumliens.ringring.repo;

import com.azure.spring.data.cosmos.core.mapping.EnableCosmosAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import se.kumliens.ringring.security.UserSession;

import java.util.Optional;

@Configuration
@EnableCosmosAuditing
public class CosmosConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider(UserSession userSession) {
        return () -> Optional.ofNullable(userSession != null ? userSession.getOAuthUser().email() : "n/as");
    }
}
