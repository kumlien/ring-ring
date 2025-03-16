package se.kumliens.ringring.ui.views.registration;

import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.web.client.HttpClientErrorException;
import se.kumliens.ringring.model.SubscriptionPlan;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.security.UserSession;
import se.kumliens.ringring.service.elks.ElksService;

public class TenantAccordionPanel extends AccordionPanel {

    public TenantAccordionPanel (UserSession userSession, Binder<Tenant> tenantBinder, AccordionPanel nextPanel, ElksService elksService) {
        setSummaryText("Organisation - allmänt");
        var form = new FormLayout();
        var name = new TextField("Namn");
        name.setValue(userSession.getOAuthUser().domain().substring(0, userSession.getOAuthUser().domain().indexOf(".")));
        tenantBinder.forField(name).asRequired("Ett namn behövs nog").bind(Tenant::getName, Tenant::setName);

        var domain = new TextField("Domän (ej ändringsbart)");
        domain.setValue(userSession.getOAuthUser().domain());
        domain.setReadOnly(true);
        tenantBinder.forField(domain).bindReadOnly(Tenant::getDomain);

        var elks_id = new TextField("46-Elks client id");
        tenantBinder.forField(elks_id).bind(Tenant::getElkId, Tenant::setElkId);

        var elks_secret = new PasswordField("46-Elks client secret");
        elks_secret.setTooltipText("Alla hemlisar sparas i Azure Key Cloak");
        tenantBinder.forField(elks_secret).bind(Tenant::getElkSecret, Tenant::setElkSecret);

        // Add a "Validate" button
        var validateButton = new Button("Testa 46-elks", event -> {
            String clientId = elks_id.getValue();
            String clientSecret = elks_secret.getValue();

            // Call the 3rd party API for validation
            boolean isValid = validateElksConnection(clientId, clientSecret, elksService);

            // Show result to the user
            if (isValid) {
                var n = Notification.show("Lyckad koppling till älgarna!", 3000, Notification.Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                var n = Notification.show("Det gick så där. Försök med nya användaruppgifter", 10_000, Notification.Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        validateButton.setEnabled(false); // Initially disabled

        // Enable the button only when both fields are populated
        elks_id.addValueChangeListener(event -> validateButton.setEnabled(
                !elks_id.isEmpty() && !elks_secret.isEmpty()
        ));
        elks_secret.addValueChangeListener(event -> validateButton.setEnabled(
                !elks_id.isEmpty() && !elks_secret.isEmpty()
        ));

        var subscriptionPlan = new Select<SubscriptionPlan>();
        subscriptionPlan.setLabel("Subscription plan");
        subscriptionPlan.setItems(SubscriptionPlan.values());
        subscriptionPlan.setItemLabelGenerator(SubscriptionPlan::name);
        tenantBinder.forField(subscriptionPlan).asRequired("Vi behöver en subscription plan").bind(Tenant::getSubscriptionPlan, Tenant::setSubscriptionPlan);

        // Add navigation buttons
        var nextButton = new Button("Nästa", event -> nextPanel.setOpened(true));
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        tenantBinder.addStatusChangeListener(s -> nextButton.setEnabled(s.getBinder().isValid()));

        form.add(name, domain, elks_id, elks_secret, subscriptionPlan);
        add( new VerticalLayout(form, new HorizontalLayout(nextButton, validateButton)));
    }

    private boolean validateElksConnection(String clientId, String clientSecret, ElksService elksService) {
        try {
            return elksService.getAccount(clientId, clientSecret) != null;
        } catch (HttpClientErrorException.Unauthorized u) {
            return false;
        }
    }
}
