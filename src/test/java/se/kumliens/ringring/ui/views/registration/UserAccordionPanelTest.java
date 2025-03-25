package se.kumliens.ringring.ui.views.registration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserAccordionPanelTest {


    private static final String E164_REGEX = "^\\+[1-9]\\d{1,14}$";

    @Test
    public void testValidPhoneNumbers() {
        // Valid E.164 phone numbers
        assertTrue("+46720796541".matches(E164_REGEX)); // Swedish number
        assertTrue("+1234567890".matches(E164_REGEX));
        assertTrue("+19876543210".matches(E164_REGEX));
        assertTrue("+447911123456".matches(E164_REGEX));
        assertTrue("+12".matches(E164_REGEX)); // Minimum valid length
        assertTrue("+123456789012345".matches(E164_REGEX)); // Maximum valid length
    }

    @Test
    public void testInvalidPhoneNumbers() {
        // Invalid E.164 phone numbers
        assertFalse("46720796541".matches(E164_REGEX)); // Missing "+"
        assertFalse("+01234567890".matches(E164_REGEX)); // Country code starts with 0
        assertFalse("+1234567890123456".matches(E164_REGEX)); // Exceeds maximum length
        assertFalse("+1".matches(E164_REGEX)); // Only "+"
        assertFalse("+1abc234567890".matches(E164_REGEX)); // Contains non-numeric characters
        assertFalse("".matches(E164_REGEX)); // Empty string
    }
}