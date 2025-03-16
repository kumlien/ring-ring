package se.kumliens.ringring.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.service.TenantService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j

/**
 * Registered in the {@link SecurityConfig#configure(HttpSecurity)} method
 */
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final TenantService tenantService;

    private final UserSession userSession;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String domain = userSession.getOAuthUser().domain();
        log.info("Start login for user {}", userSession.getOAuthUser().email());

        if (isBlockedDomain(domain)) {
            //response.sendRedirect("/blocked-domain");
            //return;
            log.info("Domain is blocked, re-route later...");
        }

        Optional<Tenant> tenant = tenantService.getTenantByDomain(domain);
        if (tenant.isEmpty()) {
            log.info("No tenant found for domain {}, redirecting to register-tenant", domain);
            response.sendRedirect("/register-tenant");
        } else {
            log.info("Tenant found for domain {}", domain);
            userSession.setTenant(tenant.get());
            var user = tenant.get().getUsers().stream()
                    .filter(u -> u.getEmail().equals(userSession.getOAuthUser().email()))
                    .findFirst();

            if (user.isEmpty()) {
                log.info("No registered user found with email {} in tenant {}", userSession.getOAuthUser().email(), tenant.get().getName());
                response.sendRedirect("/register-user");
            } else {
                log.info("Tenant and user found, send to root");
                userSession.setUser(user.get());
                response.sendRedirect("/");
            }
        }
    }

    private boolean isBlockedDomain(String domain) {
        List<String> blockedDomains = List.of("gmail.com", "yahoo.com", "outlook.com");
        return blockedDomains.contains(domain.toLowerCase());
    }
}
