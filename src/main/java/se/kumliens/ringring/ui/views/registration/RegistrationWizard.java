package se.kumliens.ringring.ui.views.registration;

import com.azure.security.keyvault.secrets.SecretClient;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import lombok.extern.slf4j.Slf4j;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.model.User;
import se.kumliens.ringring.security.UserSession;

@Slf4j
public class RegistrationWizard extends VerticalLayout {

    Accordion accordion = new Accordion();

    //ctor
    public RegistrationWizard(UserSession userSession, SecretClient secretClient) {
        var tenantBinder = new Binder<>(Tenant.class);
        var userBinder = new Binder<>(User.class);

        var orgName = userSession.getUser().domain().substring(0, userSession.getUser().domain().indexOf("."));
        H1 header = new H1("Registrering");
        Span text = new Span();
        var summaryPanel = createSummaryPanel(tenantBinder, userBinder);
        var userInfoPanel = createUserInfoPanel(userSession, userBinder, summaryPanel);
        if(userSession.tenant == null) {
            text = new Span("Hej " + userSession.getUser().firstName()  +
                    ". Du är den första från '" + orgName + "' som hittat hit! \n" +
                    "Ange lite info om din organisation '" + orgName + "' och om dig.");
            accordion.add(createTenantInfoPanel(userSession, tenantBinder, userInfoPanel));
        }
        accordion.add(userInfoPanel);
        accordion.add(summaryPanel);

        add(new VerticalLayout(header, text, accordion));
    }

    private AccordionPanel createTenantInfoPanel(UserSession userSession, Binder<Tenant> tenantBinder, AccordionPanel nextPanel) {
        var form = new FormLayout();
        var name = new TextField("Namn");
        name.setValue(userSession.getUser().domain().substring(0, userSession.getUser().domain().indexOf(".")));
        tenantBinder.forField(name).asRequired("Ett namn behövs nog").bind(Tenant::name, Tenant::name);

        var domain = new TextField("Domän (ej ändringsbart)");
        domain.setValue(userSession.getUser().domain());
        domain.setReadOnly(true);
        tenantBinder.forField(domain).bind(Tenant::domain, Tenant::domain);

        var elks_id = new TextField("46-Elks client id");
        tenantBinder.forField(elks_id).bind(Tenant::elkId, Tenant::elkId);

        var elks_secret = new PasswordField("46-Elks client secret");
        elks_secret.setTooltipText("Alla hemlisar sparas i Azure Key Cloak");
        tenantBinder.forField(elks_secret).bind(Tenant::elkSecret, Tenant::elkSecret);

        // Add navigation buttons
        var nextButton = new Button("Nästa", event -> {
            nextPanel.setOpened(true);
        });
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        form.add(name, domain, elks_id, elks_secret, new HorizontalLayout(nextButton));
        return new AccordionPanel("Organisation", form);
    }


    private AccordionPanel createUserInfoPanel(UserSession userSession, Binder<User> binder, AccordionPanel nextPanel) {
        var form = new FormLayout();

        var firstName = new TextField("Förnamn");
        binder.forField(firstName).bind(User::firstName, User::firstName);
        // Add navigation buttons
        var nextButton = new Button("Nästa", event -> {
            nextPanel.setOpened(true);
        });
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        form.add(firstName, new HorizontalLayout(nextButton));
        return new AccordionPanel("Användare", form);
    }

    private AccordionPanel createSummaryPanel(Binder<Tenant> tenantBinder, Binder<User> userBinder) {
        var form = new FormLayout();
        var ok_btn = new Button("Go!", e -> {
            var tenant = new Tenant();
            if (tenantBinder.writeBeanIfValid(tenant)) {
                Notification.show("Tenant är ok...");
            } else {
                Notification.show("Valideringsfel...");
            }
        });
        // Add form fields for user info
        // Example: TextField firstName = new TextField("First Name");
        return new AccordionPanel("Sammanfattning", new VerticalLayout(form, new HorizontalLayout(ok_btn)));
    }
}
