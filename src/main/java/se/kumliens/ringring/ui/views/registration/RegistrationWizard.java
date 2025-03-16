package se.kumliens.ringring.ui.views.registration;

import com.azure.security.keyvault.secrets.SecretClient;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import lombok.extern.slf4j.Slf4j;
import se.kumliens.ringring.model.Role;
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
        var userInfoPanel = createUserInfoPanel(userSession, userBinder, summaryPanel);
        if(userSession.getTenant() == null) {
            text = new Span("Hej " + userSession.getOAuthUser().firstName()  +
                    ". Du är den första från '" + orgName + "' som hittat hit! \n" +
                    "Ange lite info om din organisation '" + orgName + "' och om dig.");
            accordion.add(new TenantAccordionPanel(userSession, tenantBinder, userInfoPanel, elksService));
        }
        accordion.add(userInfoPanel);
        accordion.add(summaryPanel);

        add(new VerticalLayout(header, text, accordion));
    }


    private AccordionPanel createUserInfoPanel(UserSession userSession, Binder<User> binder, AccordionPanel nextPanel) {
        var form = new FormLayout();
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("450px", 2));
        var firstName = new TextField("Förnamn");
        binder.forField(firstName).asRequired("Vi behöver ett förnamn").bind(User::getFirstName, User::setFirstName);

        var lastName = new TextField("Efternamn");
        binder.forField(lastName).asRequired("Vi behöver ett efternamn").bind(User::getLastName, User::setLastName);

        var role = new Select<Role>();
        role.setLabel("Roll");
        role.setItems(Role.values());
        role.setItemLabelGenerator(Role::name);
        role.setReadOnly(true);
        binder.forField(role).asRequired("Roll behöver anges").bind(User::getRole, User::setRole);

        // Add navigation buttons
        var nextButton = new Button("Nästa", event -> {
            nextPanel.setOpened(true);
        });
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        form.add(firstName, lastName, role);
        return new AccordionPanel("Användare", new VerticalLayout(form, nextButton));
    }
}
