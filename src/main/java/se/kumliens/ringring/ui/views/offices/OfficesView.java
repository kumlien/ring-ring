package se.kumliens.ringring.ui.views.offices;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import se.kumliens.ringring.ui.views.MainLayout;

@Route(layout = MainLayout.class)
@PermitAll
public class OfficesView extends VerticalLayout {

    public OfficesView() {
        add(new Div("hej kontor..."));
    }
}
