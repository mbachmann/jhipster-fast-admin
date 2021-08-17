package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactReminderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactReminder.class);
        ContactReminder contactReminder1 = new ContactReminder();
        contactReminder1.setId(1L);
        ContactReminder contactReminder2 = new ContactReminder();
        contactReminder2.setId(contactReminder1.getId());
        assertThat(contactReminder1).isEqualTo(contactReminder2);
        contactReminder2.setId(2L);
        assertThat(contactReminder1).isNotEqualTo(contactReminder2);
        contactReminder1.setId(null);
        assertThat(contactReminder1).isNotEqualTo(contactReminder2);
    }
}
