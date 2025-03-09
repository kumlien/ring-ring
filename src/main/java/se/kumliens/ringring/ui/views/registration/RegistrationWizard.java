package se.kumliens.ringring.ui.views.registration;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class RegistrationWizard extends VerticalLayout {

    private final List<Div> steps = new ArrayList<>();
    private int currentStepIndex = 0;

    public RegistrationWizard() {
        // Initialize steps
        steps.add(createTenantInfoStep());
        steps.add(createUserInfoStep());
        steps.add(createConfirmationStep());

        // Show the first step
        showStep(currentStepIndex);
    }

    private Div createTenantInfoStep() {
        Div stepContent = new Div();
        stepContent.setText("Step 1: Enter Tenant Information");

        // Add form fields for tenant info
        // Example: TextField tenantName = new TextField("Tenant Name");

        // Add navigation buttons
        Button nextButton = new Button("Next", event -> navigateToStep(1));
        stepContent.add(nextButton);

        return stepContent;
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
