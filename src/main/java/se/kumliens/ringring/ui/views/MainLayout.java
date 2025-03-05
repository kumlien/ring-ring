package se.kumliens.ringring.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.router.AfterNavigationListener;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;
import se.kumliens.ringring.security.LoginHandler;
import se.kumliens.ringring.security.UserSession;
import se.kumliens.ringring.ui.components.NavigationLink;
import se.kumliens.ringring.ui.views.dashboard.DashboardView;

@Slf4j
@CssImport("./styles/main-layout.css") // Import the CSS file
public class MainLayout extends AppLayout {

    private final LoginHandler loginHandler;
    private final UserSession userSession;

    public MainLayout(UserSession userSession, LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
        this.userSession = userSession;
        var header = header(userSession);
        addToNavbar(header);
        addToDrawer(getSideNav());
    }

    private Component createAvatar(UserSession userSession) {
        var user = userSession.getUser();
        var avatar = new Avatar(user.firstName(), user.picture());

        var menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        var menuItem = menuBar.addItem(avatar);
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
        var scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        return scroller;
    }

    private HorizontalLayout header(UserSession userSession) {
        var toggle = new DrawerToggle();
        // Header
        H1 title = new H1("Ring Ring");
        title.addClassName("header-title");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
        // Create an Avatar component
        var avatar = createAvatar(userSession);

        // Layout for the header
        var header = new HorizontalLayout(toggle, title, avatar);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.expand(title);
        header.addClassName("header");

        return header;
    }

    @Override
    protected void afterNavigation() {
        loginHandler.handleLogin(userSession);
    }
}