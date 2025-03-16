package se.kumliens.ringring.ui.views.registration;

import com.azure.security.keyvault.secrets.SecretClient;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import se.kumliens.ringring.security.UserSession;
import se.kumliens.ringring.service.TenantService;
import se.kumliens.ringring.service.elks.ElksService;


//Redirected here by CustomAuthSuccessHandler when user has no org registered
@PageTitle("Registrera din organisation")
@Route(value = "register-tenant")
@PermitAll
public class TenantRegistrationView extends VerticalLayout implements BeforeEnterObserver {

    private final UserSession userSession;

    public TenantRegistrationView(UserSession userSession, SecretClient secretClient, ElksService elksService, TenantService tenantService) {
        this.userSession = userSession;
        add(new RegistrationWizard(userSession, secretClient, elksService, tenantService));
    }

    //Simple guard to prevent access to this view by typing the url
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(userSession.getTenant() != null) {
            event.forwardTo("/");
        }
    }
}
