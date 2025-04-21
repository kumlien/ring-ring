package se.kumliens.ringring.ui.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import jakarta.annotation.security.PermitAll;
import se.kumliens.ringring.security.UserSession;
import se.kumliens.ringring.ui.views.MainLayout;

@PageTitle("Dashboard | RingRing")
@Route(value = "", layout = MainLayout.class)
@CssImport("./styles/dashboard-view.css") // Import the CSS file for styling
@PermitAll
public class DashboardView extends VerticalLayout {

    public DashboardView(UserSession userSession) {
        addClassName("dashboard-view"); // Add a CSS class to the root container

        initUi(userSession);
    }

    private void initUi(UserSession userSession) {
        removeAll();
        // Title
        H1 title = new H1("Välkommen " + userSession.getOAuthUser().firstName());
        title.addClassName("dashboard-title");

        var cardlayout = new FlexLayout();
        cardlayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        cardlayout.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER);
        cardlayout.getStyle().set("gap", "20px"); // Add spacing between cards
        cardlayout.add(createOfficesCard(userSession));
        cardlayout.add(createVirtualNumberCard(userSession));

        // Add components to the view
        add(title, cardlayout);
    }

    //Create the card showing the virtual numbers configured for the tenant
    private Card createVirtualNumberCard(UserSession userSession) {
        var card = new Card();
        card.addThemeVariants(CardVariant.LUMO_COVER_MEDIA, CardVariant.LUMO_ELEVATED, CardVariant.LUMO_OUTLINED);
        var image = new Image("images/connections.jpg","Virtual connections");
        image.setHeight("120px");
        card.setMedia(image);
        card.setTitle(new Div("Dina virtuella nummer"));
        card.setSubtitle(new Div("Klicka för detaljer"));

        for(var vn : userSession.getTenant().getVirtualNumbers()) {
            card.add(new Div(vn.name() + ": " + vn.number()));
        }

        return card;
    }

    private Card createOfficesCard(UserSession userSession) {
        var card = new Card();
        card.addThemeVariants(CardVariant.LUMO_COVER_MEDIA, CardVariant.LUMO_ELEVATED, CardVariant.LUMO_OUTLINED);
        var image = new Image("images/green_office.jpg","Green Office Building");
        image.setHeight("120px");
        card.setMedia(image);
        card.setTitle(new Div("Dina kontor"));
        card.setSubtitle(new Div("Klicka för detaljer"));

        for(var office : userSession.getTenant().getOffices()) {
            var layout = new HorizontalLayout();
            var name = new Div(office.getName() + ", " + office.getAddress());
            layout.expand(name);
            layout.add(name);
            if(office.hasVirtualNumber()) {
                Span badge = new Span();
                badge.getElement().getThemeList().add("badge success");
                badge.add(VaadinIcon.CHECK_CIRCLE.create());
                var icon = LumoIcon.CHECKMARK.create();
                icon.setColor("green");
                layout.add(icon);
            } else {
                var icon = LumoIcon.CROSS.create();
                icon.setColor("red");
                layout.add(icon);
            }
            card.add(layout);
        }

        Button bookVacationButton = new Button("Skapa nytt kontor");
        Button learnMoreButton = new Button("Visa alla");
        card.addToFooter(bookVacationButton, learnMoreButton);
        return card;
    }
}
