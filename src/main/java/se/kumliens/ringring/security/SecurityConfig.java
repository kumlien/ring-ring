package se.kumliens.ringring.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity
//@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends VaadinWebSecurity {

    private final CustomAuthSuccessHandler authSuccessHandler;

    //@Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        // Configure security for REST API endpoints
        http.securityMatcher("/api/**") // Apply this configuration only to /api/** endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated() // Require authentication for /api/** endpoints
                )
                .oauth2ResourceServer(conf -> conf.jwt(Customizer.withDefaults())); // Enable JWT validation for APIs

        return http.build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Apply Vaadin's default security configuration for all other requests
        super.configure(http);

        // Configure OAuth2 login for Vaadin pages
        http.oauth2Login(oauth2 -> oauth2.loginPage("/login").successHandler(authSuccessHandler).permitAll());
    }
}
