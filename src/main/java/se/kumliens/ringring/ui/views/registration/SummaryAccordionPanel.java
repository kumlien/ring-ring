package se.kumliens.ringring.ui.views.registration;

import com.azure.security.keyvault.secrets.SecretClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import lombok.extern.slf4j.Slf4j;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.model.User;
import se.kumliens.ringring.security.UserSession;
import se.kumliens.ringring.service.TenantService;

import java.time.LocalDateTime;

/**
 * Panel with a summary of what to register (tenant, user)
 * Also contains the button triggering the
 */
@Slf4j
public class SummaryAccordionPanel extends AccordionPanel {

    public SummaryAccordionPanel (Binder<Tenant> tenantBinder, Binder<User> userBinder, TenantService tenantService, UserSession userSession, SecretClient secretClient) {
        setSummaryText("Sammanfattning");
        var textLayout = createTextLayout(tenantBinder, userBinder);

        var ok_btn = new Button("Registrera!", e -> {
            var tenant = tenantBinder != null ? tenantBinder.getBean() : userSession.getTenant();
            var user = userBinder.getBean();
            user.setCreatedAt(LocalDateTime.now());
            tenant.addUser(user);
            var secret = secretClient.setSecret(sanitizeSecretName(tenant.getDomain() + "/46Elks"), tenant.getElkSecret());
            tenant.setElkSecret(secret.getName());
            tenant = tenantService.upsert(tenant);
            userSession.setTenant(tenant);
            userSession.setUser(user);
            UI.getCurrent().navigate("");
        });
        ok_btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        if(tenantBinder != null) tenantBinder.addStatusChangeListener(s -> ok_btn.setEnabled(isValid(tenantBinder, userBinder)));
        userBinder.addStatusChangeListener(s -> ok_btn.setEnabled(isValid(tenantBinder, userBinder)));
        var layout = new VerticalLayout(textLayout, new HorizontalLayout(ok_btn));
        add(layout);
        addOpenedChangeListener(evt -> {
            if(evt.isOpened()) {
                layout.replace(layout.getComponentAt(0), createTextLayout(tenantBinder, userBinder));
            }
        });
    }

    public static String sanitizeSecretName(String name) {
        return name.replaceAll("[^a-zA-Z0-9-]", "-");
    }

    private static boolean isValid(Binder<Tenant> tenantBinder, Binder<User> userBinder) {
        return userBinder.isValid() && (tenantBinder == null || tenantBinder.isValid());
    }

    private static VerticalLayout createTextLayout(Binder<Tenant> tenantBinder, Binder<User> userBinder) {
        var textLayout = new VerticalLayout();
        var header = new H3("Sammanfattning");
        if(!isValid(tenantBinder, userBinder)) {
            textLayout.add(new H3("Något saknas i formulären, gå igenom uppgifterna igen."));
            return textLayout;
        }
        textLayout.add(header);

        var userDiv = new Div("Ny användare");
        var userDivText = "En ny användare skapas, nämligen du " + userBinder.getBean().getFirstName() + ".";
        if(tenantBinder != null) {
            var orgDiv = new Div();
            orgDiv.setTitle("Ny organisation");
            var orgName = tenantBinder.getBean().getName();
            var domain = tenantBinder.getBean().getDomain();
            orgDiv.setText("En ny organisation '" + orgName + "' med domänen  '" + domain + "' skapas.");
            textLayout.add(orgDiv);
            userDivText += " Eftersom du är den som registrerar din organisation blir du automatiskt administratör för '" + tenantBinder.getBean().getName() + "'";
        }
        userDiv.setText(userDivText);
        textLayout.add(userDiv);
        textLayout.add(new Span("Om allt ser bra ut kan du gå vidare med registreringen. Du kommer sedan skickas till huvudsidan."));
        return textLayout;
    }
}
