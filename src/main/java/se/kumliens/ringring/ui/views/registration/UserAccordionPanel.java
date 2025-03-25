package se.kumliens.ringring.ui.views.registration;

import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import se.kumliens.ringring.model.Role;
import se.kumliens.ringring.model.User;

public class UserAccordionPanel extends AccordionPanel {

    UserAccordionPanel (Binder<User> binder, AccordionPanel nextPanel) {
        super();
        setSummaryText("Din användare");

        var form = new FormLayout();
        //form.setResponsiveSteps(new FormLayout.ResponsiveStep("450px", 2));

        var firstName = new TextField("Ditt förnamn");
        binder.forField(firstName).asRequired("Vi behöver ett förnamn").bind(User::getFirstName, User::setFirstName);

        var lastName = new TextField("Ditt efternamn");
        binder.forField(lastName).asRequired("Vi behöver ett efternamn").bind(User::getLastName, User::setLastName);

        var phoneNumber = new TextField("Ditt telefonnummer");
        phoneNumber.setPattern("^\\+[1-9]\\d{1,14}$");
        binder.forField(phoneNumber).asRequired("Du måste ange ditt telefonnummer").bind(User::getPhoneNumber, User::setPhoneNumber);

        var role = new Select<Role>();
        role.setLabel("Roll");
        role.setItems(Role.values());
        role.setItemLabelGenerator(Role::name);
        role.setReadOnly(true);
        binder.forField(role).asRequired("Roll behöver anges").bind(User::getRole, User::setRole);

        // Add navigation buttons
        var nextButton = new Button("Nästa", event -> nextPanel.setOpened(true));
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        form.add(firstName, lastName, phoneNumber, role);
        add( new VerticalLayout(form, nextButton));
    }
}
