package se.kumliens.ringring.ui.views.registration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SummaryAccordionPanelTest {

    @Test
    public void testSanitize() {
        var in = "gmail.com-46Elks";
        var out = SummaryAccordionPanel.sanitizeSecretName(in);
        assertEquals("gmail-com-46Elks", out);
    }
}