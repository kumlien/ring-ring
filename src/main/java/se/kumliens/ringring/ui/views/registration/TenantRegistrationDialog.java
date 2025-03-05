package se.kumliens.ringring.ui.views.registration;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.model.User;
import se.kumliens.ringring.security.UserSession;
import se.kumliens.ringring.service.TenantService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class TenantRegistrationDialog extends Dialog {

    public TenantRegistrationDialog(TenantService tenantService, UserSession userSession) {

        // Retrieve email and domain from the session
        String email = userSession.getUser().email();
        String domain = userSession.getUser().domain();

        // Create form fields
        var tenantNameField = new TextField("Organisation name");

        // Create a form layout
        FormLayout formLayout = new FormLayout();
        formLayout.add(tenantNameField);

        // Create a save button
        var saveButton = new Button("Register Tenant", event -> {
            if (tenantNameField.isEmpty()) {
                Notification.show("Please fill in all fields.");
                return;
            }

            // Save the tenant to the database
            Tenant tenant = new Tenant(
                    UUID.randomUUID().toString(),
                    domain,
                    tenantNameField.getValue(),
                    "FREE",
                    LocalDateTime.now(),
                    new ArrayList<>(), // Empty users list
                    new ArrayList<>(), // Empty offices list
                    new ArrayList<>()  // Empty virtual numbers list
            );
            tenantService.upsert(tenant);

            // Add the user to the tenant
            User user = new User(
                    UUID.randomUUID().toString(),
                    email,
                    userSession.getUser().firstName(),
                    userSession.getUser().lastName(),
                    "ADMIN", // Assign the first user as ADMIN
                    "ACTIVE",
                    LocalDateTime.now()
            );
            tenant.users().add(user);
            tenantService.upsert(tenant);

            // Close the dialog
            Notification.show("Tenant registered successfully!");
            close();
        });

        // Add components to the dialog
        VerticalLayout layout = new VerticalLayout(formLayout);
        add(layout);
        getFooter().add(saveButton);

        // Open the dialog
        setModal(true);
        setCloseOnOutsideClick(false);
        open();
    }
}
