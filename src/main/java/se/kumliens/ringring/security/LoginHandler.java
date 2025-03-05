package se.kumliens.ringring.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.service.TenantService;
import se.kumliens.ringring.ui.views.registration.TenantRegistrationDialog;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginHandler {
    private final TenantService tenantService;

    public void handleLogin(UserSession userSession) {

        // Check if tenant exists
        Optional<Tenant> tenant = tenantService.getTenantByDomain(userSession.getUser().domain());
        if (tenant.isEmpty()) {
            // Block common domains like gmail.com
            if (isBlockedDomain(userSession.getUser().domain())) {
                //throw new IllegalArgumentException("Common email domains cannot be used to register tenants.");
                UI.getCurrent().add(new Dialog("Bad domain...", new TextField("Bad domain: " + userSession.getUser().domain())));
            }
            // Defer navigation to the Vaadin UI thread
            new TenantRegistrationDialog(tenantService, userSession);
        } else {
            // Check if user exists in the tenant
            var user = tenant.get().users().stream()
                    .filter(u -> u.email().equals(userSession.getUser().email()))
                    .findFirst();

            if (user.isEmpty()) {
                // Redirect to user registration page
                UI.getCurrent().navigate("register-user");
            }
        }
    }



    private boolean isBlockedDomain(String domain) {
        List<String> blockedDomains = List.of("gmail.com", "yahoo.com", "outlook.com");
        return blockedDomains.contains(domain.toLowerCase());
    }
}
