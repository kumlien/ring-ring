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
import se.kumliens.ringring.model.Office;
import se.kumliens.ringring.model.Tenant;
import se.kumliens.ringring.model.VirtualNumber;
import se.kumliens.ringring.service.elks.ElksService;

import java.util.Collections;

public class OfficesAccordionPanel extends AccordionPanel {

    private final ElksService elksService;
    private final Tenant tenant;
    private final AccordionPanel nextPanel;


    public OfficesAccordionPanel(Tenant tenant, AccordionPanel nextPanel, ElksService elksService) {
        super("Kontor");
        this.elksService = elksService;
        this.tenant = tenant;
        this.nextPanel = nextPanel;
        addOpenedChangeListener(this::initUi);
    }

    private void initUi(OpenedChangeEvent evt) {
        removeAll();
        if(!evt.isOpened()) return;

        var form = new FormLayout();

        var binder = new Binder<>(Office.class);
        binder.setBean(tenant.getOffices().isEmpty() ? new Office() : tenant.getOffices().getFirst());

        var name = new TextField("Namn");
        binder.forField(name).asRequired("Kontoret behöver ett namn").bind(Office::getName, Office::setName);

        var address = new TextField("Adress");
        binder.forField(address).asRequired("Ge kontoret någon slags adress").bind(Office::getAddress, Office::setAddress);

        Select<VirtualNumber> virtualNumber = new Select<>();
        virtualNumber.setLabel("Virtuellt nummer");
        virtualNumber.setItems(Collections.emptyList());
        virtualNumber.setItemLabelGenerator(vr -> vr == null ? "<Inget virtuellt nummer kopplat>" : vr.name());
        virtualNumber.setEmptySelectionAllowed(true);
        binder.forField(virtualNumber).bind(Office::getVirtualNumber, Office::setVirtualNumber);

                if (tenant.hasVirtualNumbers()) {
                    virtualNumber.setItems(tenant.getVirtualNumbers());
                    virtualNumber.setValue(tenant.getVirtualNumbers().getFirst());
                }


        var saveButton = new Button("Lägg till", event -> {
            if(binder.isValid()) {
                if(!tenant.getOffices().contains(binder.getBean())) {
                    tenant.addOffice(binder.getBean());
                    Notification.show(binder.getBean().getName() + " tillagt", 2_500, Notification.Position.MIDDLE);
                }
            }
        });
        saveButton.setEnabled(false);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        // Add navigation buttons
        var nextButton = new Button("Nästa", event -> nextPanel.setOpened(true));
        nextButton.setEnabled(false);
        binder.addStatusChangeListener(statusChangeEvent -> {
            nextButton.setEnabled(binder.isValid());
            saveButton.setEnabled(binder.isValid());
        });

        form.add(name, address, virtualNumber);
        add(new VerticalLayout(new Span("Ange info för ditt kontor"), form, new HorizontalLayout(saveButton, nextButton)));
    }
}
