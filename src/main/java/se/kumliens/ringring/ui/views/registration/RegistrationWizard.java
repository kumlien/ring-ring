package se.kumliens.ringring.ui.views.registration;

import com.azure.security.keyvault.secrets.SecretClient;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import lombok.extern.slf4j.Slf4j;
import se.kumliens.ringring.model.SubscriptionPlan;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.model.User;
import se.kumliens.ringring.security.UserSession;
import se.kumliens.ringring.service.TenantService;
import se.kumliens.ringring.service.elks.ElksService;

import static se.kumliens.ringring.model.Role.ADMIN;
import static se.kumliens.ringring.model.Role.USER;

@Slf4j
public class RegistrationWizard extends VerticalLayout {

    Accordion accordion = new Accordion();

    //ctor
    public RegistrationWizard(UserSession userSession, SecretClient secretClient, ElksService elksService, TenantService tenantService) {
        accordion.setSizeFull();
        var orgName = userSession.getOAuthUser().domain().substring(0, userSession.getOAuthUser().domain().indexOf("."));
        var tenantBinder = new Binder<>(Tenant.class);
        tenantBinder.setBean(new Tenant());
        tenantBinder.getBean().setDomain(userSession.getOAuthUser().domain());
        tenantBinder.getBean().setName(orgName);
        tenantBinder.getBean().setSubscriptionPlan(SubscriptionPlan.FREE);

        var userBinder = new Binder<>(User.class);
        userBinder.setBean(new User());
        userBinder.getBean().setEmail(userSession.getOAuthUser().email());
        userBinder.getBean().setFirstName(userSession.getOAuthUser().firstName());
        userBinder.getBean().setLastName(userSession.getOAuthUser().lastName());
        userBinder.getBean().setRole(userSession.getTenant() == null ? ADMIN : USER);

        H1 header = new H1("Registrering");
        Span text = new Span();
        var summaryPanel = new SummaryAccordionPanel(tenantBinder, userBinder, tenantService, userSession, secretClient);
        var userInfoPanel = new UserAccordionPanel(userBinder, summaryPanel);

        if(userSession.getTenant() == null) {
            text = new Span("Hej " + userSession.getOAuthUser().firstName()  +
                    ". Du är den första från '" + orgName + "' som hittat hit! \n" +
                    "Ange lite info om din organisation '" + orgName + "' och om dig.");
            var officePanel = new OfficesAccordionPanel(tenantBinder.getBean(), userInfoPanel, elksService);
            accordion.add(new TenantAccordionPanel(userSession, tenantBinder, officePanel, elksService));
            accordion.add(officePanel);
        }

        accordion.add(userInfoPanel);
        accordion.add(summaryPanel);

        add(new VerticalLayout(header, text, accordion));
    }

}
