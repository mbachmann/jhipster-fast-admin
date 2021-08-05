package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactReminderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactReminderDTO.class);
        ContactReminderDTO contactReminderDTO1 = new ContactReminderDTO();
        contactReminderDTO1.setId(1L);
        ContactReminderDTO contactReminderDTO2 = new ContactReminderDTO();
        assertThat(contactReminderDTO1).isNotEqualTo(contactReminderDTO2);
        contactReminderDTO2.setId(contactReminderDTO1.getId());
        assertThat(contactReminderDTO1).isEqualTo(contactReminderDTO2);
        contactReminderDTO2.setId(2L);
        assertThat(contactReminderDTO1).isNotEqualTo(contactReminderDTO2);
        contactReminderDTO1.setId(null);
        assertThat(contactReminderDTO1).isNotEqualTo(contactReminderDTO2);
    }
}
