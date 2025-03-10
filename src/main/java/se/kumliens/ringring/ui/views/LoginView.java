package se.kumliens.ringring.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PageTitle("Login | RingRing")
@Route("login")
@AnonymousAllowed
@CssImport("./styles/login-view.css") // Import the CSS file
public class LoginView extends Div {

    // URL that Spring uses to connect to Google services
    private static final String OAUTH_URL = "/oauth2/authorization/google";

    public LoginView() {
        addClassName("login-view");

        // Title
        H1 title = new H1("Logga in to Ring Ring");
        title.addClassName("login-title");

        // Login Button
        Button loginButton = new Button("Login with Google", e -> {
            // Redirect to Google login
            UI.getCurrent().getPage().setLocation(OAUTH_URL);
        });
        loginButton.addClassName("login-button");

        // Add components to the view
        add(title, loginButton);
    }
}
