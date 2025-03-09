package se.kumliens.ringring.ui.views.registration;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Registrera din organisation")
@Route(value = "register-tenant")
@PermitAll
public class TenantRegistrationView extends VerticalLayout {

    public TenantRegistrationView() {
        add(new RegistrationWizard());
    }
}
