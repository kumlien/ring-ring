package se.kumliens.ringring.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.sidenav.SideNavItem;

//@CssImport("./styles/navigation-link.css") // Import the CSS file
public class NavigationLink extends SideNavItem {

    public NavigationLink(String text, Class<?extends Component> navigationTarget) {
        super(text, navigationTarget);
        // Add a common CSS class for all navigation links
        //addClassName("navigation-link");
    }

    public NavigationLink(String text, Class<?extends Component> navigationTarget, Icon icon) {
        this(text, navigationTarget);
        setPrefixComponent(icon);
    }
}
