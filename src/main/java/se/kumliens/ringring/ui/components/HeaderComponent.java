package se.kumliens.ringring.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;

public class HeaderComponent extends Div {

    public HeaderComponent(String title) {
        H1 header = new H1(title);
        add(header);
    }
}
