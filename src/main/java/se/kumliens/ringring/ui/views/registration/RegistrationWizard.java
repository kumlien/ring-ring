package se.kumliens.ringring.ui.views.registration;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import se.kumliens.ringring.security.UserSession;

import java.util.ArrayList;
import java.util.List;

public class RegistrationWizard extends VerticalLayout {

    private final List<Component> steps = new ArrayList<>();
    private int currentStepIndex = 0;

    public RegistrationWizard(UserSession userSession) {
        // Initialize steps
        if(userSession.tenant == null) {
            steps.add(createTenantInfoStep(userSession));
        }
        steps.add(createUserInfoStep());
        steps.add(createConfirmationStep());

        // Show the first step
        showStep(currentStepIndex);
    }

    private Component createTenantInfoStep(UserSession userSession) {
        var orgName = userSession.getUser().domain().substring(0, userSession.getUser().domain().indexOf("."));
        var header = new H1("Registrering av  '" + orgName +"'");
        var text = new Span("Hej " + userSession.getUser().firstName()  +
                ". Du är den första från " + orgName + " som hittat hit. " +
                "Börja med att ange lite info om " + orgName + ".");

        var form = getOrganisationRegistrationForm(userSession);
        var layout = new VerticalLayout(header, text, form);

        return layout;
    }

    private FormLayout getOrganisationRegistrationForm(UserSession userSession) {
        var form = new FormLayout();

        var name = new TextField("Organisationens namn");
        name.setValue(userSession.getUser().domain().substring(0, userSession.getUser().domain().indexOf(".")));

        var domain = new TextField("Organisationens domän (ej ändringsbart)");
        domain.setValue(userSession.getUser().domain());
        domain.setReadOnly(true);

        var elks_id = new TextField("46-Elks client id");
        var elks_secret = new PasswordField("46-Elks client secret");
        elks_secret.setTooltipText("Alla hemlisar sparas i Azure Key Cloak");

        // Add navigation buttons
        var nextButton = new Button("Nästa", event -> navigateToStep(1));
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        var cancelButton = new Button("Avbryt", event -> userSession.logout());

        form.add(name, domain, elks_id, elks_secret, new HorizontalLayout(nextButton, cancelButton));
        return form;
    }

    private Div createUserInfoStep() {
        Div stepContent = new Div();
        stepContent.setText("Step 2: Enter User Information");

        // Add form fields for user info
        // Example: TextField firstName = new TextField("First Name");

        // Add navigation buttons
        Button backButton = new Button("Back", event -> navigateToStep(0));
        Button nextButton = new Button("Next", event -> navigateToStep(2));
        stepContent.add(backButton, nextButton);

        return stepContent;
    }

    private Div createConfirmationStep() {
        Div stepContent = new Div();
        stepContent.setText("Step 3: Confirm and Submit");

        // Add a "Submit" button to complete the wizard
        Button backButton = new Button("Back", event -> navigateToStep(1));
        Button submitButton = new Button("Submit", event -> {
            // Handle submission logic here
            System.out.println("Wizard completed!");
        });
        stepContent.add(backButton, submitButton);

        return stepContent;
    }

    private void showStep(int stepIndex) {
        removeAll(); // Clear the layout
        add(steps.get(stepIndex)); // Add the current step
    }

    private void navigateToStep(int stepIndex) {
        if (stepIndex >= 0 && stepIndex < steps.size()) {
            currentStepIndex = stepIndex;
            showStep(currentStepIndex);
        }
    }
}
