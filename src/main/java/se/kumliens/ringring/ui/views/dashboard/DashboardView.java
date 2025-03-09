package se.kumliens.ringring.ui.views.dashboard;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.*;
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
        H1 title = new H1("Välkommen " + userSession.getUser().firstName() + " till Ring-Ring!");
        title.addClassName("dashboard-title");

        // Description
        Paragraph description = new Paragraph("Här finns galet mycket central o viktig info för dig!");
        description.addClassName("dashboard-description");

        // Add components to the view
        add(title, description);
    }
}
