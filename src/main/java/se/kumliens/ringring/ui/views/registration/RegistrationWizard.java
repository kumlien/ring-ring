package se.kumliens.ringring.ui.views.registration;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;
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
        var header = new H1("Registrera " + orgName);
        var text = new Span("Du är den första från " + orgName + " som hittat hit, dags för registrering!");
        text.addClassName(LumoUtility.FontSize.LARGE);

        var form = getOrganisationRegistrationForm(userSession);
        return new VerticalLayout(header, text, form);
    }

    private FormLayout getOrganisationRegistrationForm(UserSession userSession) {
        var form = new FormLayout();
        var name = new TextField("Organisationens namn");
        name.setValue(userSession.getUser().domain().substring(0, userSession.getUser().domain().indexOf(".")));
        var domain = new TextField("Organisationens domän (en ändringsbart)");
        domain.setValue(userSession.getUser().domain());
        domain.setReadOnly(true);

        // Add navigation buttons
        Button nextButton = new Button("Nästa", event -> navigateToStep(1));
        form.add(name, domain, nextButton);
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
