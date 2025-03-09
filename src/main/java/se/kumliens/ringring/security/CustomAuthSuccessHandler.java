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
        String domain = userSession.getUser().domain();

        if (isBlockedDomain(domain)) {
            //response.sendRedirect("/blocked-domain");
            //return;
        }

        Optional<Tenant> tenant = tenantService.getTenantByDomain(domain);
        if (tenant.isEmpty()) {
            response.sendRedirect("/register-tenant");
        } else {
            var user = tenant.get().users().stream()
                    .filter(u -> u.email().equals(userSession.getUser().email()))
                    .findFirst();

            if (user.isEmpty()) {
                response.sendRedirect("/register-user");
            } else {
                response.sendRedirect("/dashboard");
            }
        }
    }

    private boolean isBlockedDomain(String domain) {
        List<String> blockedDomains = List.of("gmail.com", "yahoo.com", "outlook.com");
        return blockedDomains.contains(domain.toLowerCase());
    }
}
