package se.kumliens.ringring.ui.views.registration;

import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.apache.logging.log4j.util.Strings;
import se.kumliens.ringring.model.Office;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.model.VirtualNumber;
import se.kumliens.ringring.service.elks.ElksService;

import java.util.List;

public class OfficesAccordionPanel extends AccordionPanel {

    public OfficesAccordionPanel(Tenant tenant, AccordionPanel nextPanel, ElksService elksService) {
        super();
        setSummaryText("Kontor");
        var binder = new Binder<>(Office.class);
        binder.setBean(new Office());
        var form = new FormLayout();

        var name = new TextField("Namn");
        binder.forField(name).asRequired("Kontoret behöver ett namn").bind(Office::getName, Office::setName);

        var address = new TextField("Adress");
        binder.forField(address).asRequired("Ge kontoret någon slags adress").bind(Office::getAddress, Office::setAddress);

        var virtualNumber = new Select<VirtualNumber>();
        virtualNumber.setLabel("Virtuellt nummer");
        virtualNumber.setItemLabelGenerator(VirtualNumber::name);
        binder.forField(virtualNumber).bind(Office::getVirtualNumber, Office::setVirtualNumber);
        addOpenedChangeListener(evt -> {
            if(evt.isOpened() && Strings.isNotBlank(tenant.getElkId()) && Strings.isNotBlank(tenant.getElkSecret())) {
                var numbers = elksService.getVirtualNumbers(tenant.getElkId(), tenant.getElkSecret());
                if(!numbers.isEmpty()) {
                    virtualNumber.setItems(elksService.getVirtualNumbers(tenant.getElkId(), tenant.getElkSecret()));
                    virtualNumber.setValue(numbers.getFirst());
                }
            }
        });

        var saveButton = new Button("Spara", event -> {
            List<VirtualNumber> numbers = null;
            numbers = elksService.getVirtualNumbers(tenant.getElkId(), tenant.getElkSecret());
            if ( numbers != null &&  !numbers.isEmpty()) {
                Notification.show(numbers.getFirst().toString() );
            }
            if(binder.isValid()) {
                tenant.addOffice(binder.getBean());
                Notification.show("Kontor tillagt", 2_000, Notification.Position.BOTTOM_CENTER);
            }
        });
        saveButton.setEnabled(false);

        // Add navigation buttons
        var nextButton = new Button("Nästa", event -> {
            nextPanel.setOpened(true);
        });
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nextButton.setEnabled(false);
        binder.addStatusChangeListener(evt -> {
            nextButton.setEnabled(binder.isValid());
            saveButton.setEnabled(binder.isValid());
        });

        form.add(name, address, virtualNumber);
        add( new VerticalLayout(new Span("Ange info för ditt kontor"), form, new HorizontalLayout(saveButton, nextButton)));
    }
}
