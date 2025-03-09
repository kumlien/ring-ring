package se.kumliens.ringring.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.model.User;

import java.io.Serializable;

@Component
@SessionScope
@Slf4j
public class UserSession implements Serializable {

    private static final String LOGOUT_SUCCESS_URL = "/";

    //Set by
    public boolean domainIsBlocked;

    private OAuth2User oAuth2User;
    @Setter
    public User user;
    @Setter
    public Tenant tenant;

    public OAuth2User getUser() {
        if (oAuth2User == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();

            oAuth2User = new OAuth2User(principal.getAttribute("given_name"), principal.getAttribute("family_name"), principal.getAttribute("email"),
                    principal.getAttribute("picture"));
        }
        return oAuth2User;
    }

    public boolean isLoggedIn() {
        return user != null;
    }


    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        user = null;
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);

    }
}
