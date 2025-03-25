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

import java.util.function.Consumer;

public class TenantAccordionPanel extends AccordionPanel {

    public TenantAccordionPanel (UserSession userSession, Binder<Tenant> tenantBinder, AccordionPanel nextPanel, ElksService elksService) {
        super();
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
        tenantBinder.forField(elks_id).asRequired("Ange ditt API Username hos 46-Elks").bind(Tenant::getElkId, Tenant::setElkId);

        var elks_secret = new PasswordField("46-Elks client secret");
        elks_secret.setTooltipText("Alla hemlisar sparas i Azure Key Cloak");
        tenantBinder.forField(elks_secret).asRequired("Ange ditt API Password hos 46-Elks").bind(Tenant::getElkSecret, Tenant::setElkSecret);

        // Add navigation button
        var nextButton = new Button("Nästa", event -> nextPanel.setOpened(true));
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nextButton.setEnabled(false);

        // Add a "Validate" button
        var validateButton = new ValidateElksButton(elks_id, elks_secret, elksService, success -> {
            if(success) {
                var n = new Notification("Lyckad koppling till älgarna!", 3_000, Notification.Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                n.open();
                nextButton.setEnabled(tenantBinder.isValid());
            } else {
                var n = new Notification("Det gick så där. Försök med nya användaruppgifter", 3_000, Notification.Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                n.open();
                nextButton.setEnabled(false);
            }
        });

        // Enable the button only when both fields are populated
        elks_id.addValueChangeListener(event -> {
            validateButton.setEnabled(!elks_id.isEmpty() && !elks_secret.isEmpty());
            nextButton.setEnabled(false);
        });
        elks_secret.addValueChangeListener(event -> {
            validateButton.setEnabled(!elks_id.isEmpty() && !elks_secret.isEmpty());
            nextButton.setEnabled(false);
        });

        var subscriptionPlan = new Select<SubscriptionPlan>();
        subscriptionPlan.setLabel("Subscription plan");
        subscriptionPlan.setItems(SubscriptionPlan.values());
        subscriptionPlan.setItemLabelGenerator(SubscriptionPlan::name);
        tenantBinder.forField(subscriptionPlan).asRequired("Vi behöver en subscription plan").bind(Tenant::getSubscriptionPlan, Tenant::setSubscriptionPlan);

        form.add(name, domain, elks_id, elks_secret, subscriptionPlan);
        // var icon = VaadinIcon.CLOCK.create();
        //icon.getStyle().set("padding", "var(--lumo-space-xs)");
        // var validateStatus = new Span(icon, new Span("?"));
        // validateStatus.getElement().getThemeList().add("badge error small");
        add( new VerticalLayout(form, new HorizontalLayout(nextButton, validateButton)));
    }

    private boolean validateElksConnection(String clientId, String clientSecret, ElksService elksService) {
        try {
            return elksService.getAccount(clientId, clientSecret) != null;
        } catch (HttpClientErrorException.Unauthorized u) {
            return false;
        }
    }

    private class ValidateElksButton extends Button {
       public ValidateElksButton(TextField elksId, PasswordField elksSecret, ElksService elksService, Consumer<Boolean> successListener) {
            super("Check 46-Elks", event -> {
                {
                    String clientId = elksId.getValue();
                    String clientSecret = elksSecret.getValue();

                    // Call the Elks API for validation
                    boolean isValid = validateElksConnection(clientId, clientSecret, elksService);
                    // Notify the listener
                    successListener.accept(isValid);
                }
            });
        }
    }
}
