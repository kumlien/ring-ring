package se.kumliens.ringring.ui.views.dashboard;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import se.kumliens.ringring.security.UserSession;
import se.kumliens.ringring.ui.views.MainLayout;

@PageTitle("Dashboard")
@Route(value = "", layout = MainLayout.class)
@CssImport("./styles/dashboard-view.css") // Import the CSS file for styling
@PermitAll
public class DashboardView extends Div {

    public DashboardView(UserSession userSession) {
        addClassName("dashboard-view"); // Add a CSS class to the root container

        // Title
        H1 title = new H1("Welcome " + userSession.getUser().firstName() + " to the Ring Ring app!");
        title.addClassName("dashboard-title");

        // Description
        Paragraph description = new Paragraph("This is your central hub for managing the application.");
        description.addClassName("dashboard-description");

        // Add components to the view
        add(title, description);
    }
}
