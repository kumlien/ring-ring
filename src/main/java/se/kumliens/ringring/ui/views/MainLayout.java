package se.kumliens.ringring.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;
import se.kumliens.ringring.security.UserSession;
import se.kumliens.ringring.ui.components.NavigationLink;
import se.kumliens.ringring.ui.views.dashboard.DashboardView;
import se.kumliens.ringring.ui.views.offices.OfficesView;

@Slf4j
@CssImport("./styles/main-layout.css") // Import the CSS file
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private final UserSession userSession;

    public MainLayout(UserSession userSession) {
        this.userSession = userSession;
        log.info("Usersession: {}", userSession.hashCode());
        addToNavbar(header(userSession));
        addToDrawer(getSideNav());
    }

    private Component createAvatar(UserSession userSession) {
        var user = userSession.getOAuthUser();
        var avatar = new Avatar(user.firstName(), user.picture());
        var avatarDiv = new Div(avatar);
        avatarDiv.getStyle().set("padding", "10px");

        var menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        var menuItem = menuBar.addItem(avatarDiv);
        var subMenu = menuItem.getSubMenu();
        var signOut = subMenu.addItem("Sign out");
        signOut.addClickListener(evt -> userSession.logout());
        var settings = subMenu.addItem("Profile");
        return menuBar;
    }

    private Scroller getSideNav() {
        var nav = new SideNav();

        var dashboardLink = new NavigationLink("Dashboard", DashboardView.class, VaadinIcon.DASHBOARD.create());
        nav.addItem(dashboardLink);

        var officesLink = new NavigationLink("Kontor", OfficesView.class, VaadinIcon.OFFICE.create());
        nav.addItem(officesLink);

        var scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        return scroller;
    }

    //Create the header part
    private HorizontalLayout header(UserSession userSession) {
        var toggle = new DrawerToggle();
        toggle.getStyle().set("margin", "0").set("padding", "0"); // Remove margin and padding

        H1 tenant = new H1( userSession.getTenant().getDomain());
        tenant.getStyle().
                set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0")
                .set("color", "rgba(0, 0, 0, 0.2)"); // Use Vaadin's secondary text color

        var logoImg = new Image("images/logo_2.png", "App logo");
        logoImg.setHeight("44px");

        var layout_1 = new HorizontalLayout(toggle, logoImg);
        layout_1.setSpacing(false); // Remove spacing between components

        // Create an Avatar component
        var avatar = createAvatar(userSession);

        // Layout for the header
        var header = new HorizontalLayout(layout_1, tenant, avatar);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.expand(tenant);
        header.addClassName("header");

        return header;
    }

    //Needed in the register flow to make sure the user can't reach a MainLayout
    //by changing the url.
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Check if the user has completed registration
        if(userSession.domainIsBlocked) {
            log.info("Darn, domain for {} is blocked", userSession.getUser().getEmail());
        } else if (userSession.getTenant() == null) {
            log.info("No tenant in session, redirect to register-tenant");
            event.forwardTo("register-tenant");
        } else if(!userSession.isLoggedIn()) {
            log.info("No logged in user found in session, redirect to register-user");
            event.forwardTo("register-user");
        }
    }
}
