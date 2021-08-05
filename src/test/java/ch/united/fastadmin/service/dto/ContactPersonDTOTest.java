package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactPersonDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactPersonDTO.class);
        ContactPersonDTO contactPersonDTO1 = new ContactPersonDTO();
        contactPersonDTO1.setId(1L);
        ContactPersonDTO contactPersonDTO2 = new ContactPersonDTO();
        assertThat(contactPersonDTO1).isNotEqualTo(contactPersonDTO2);
        contactPersonDTO2.setId(contactPersonDTO1.getId());
        assertThat(contactPersonDTO1).isEqualTo(contactPersonDTO2);
        contactPersonDTO2.setId(2L);
        assertThat(contactPersonDTO1).isNotEqualTo(contactPersonDTO2);
        contactPersonDTO1.setId(null);
        assertThat(contactPersonDTO1).isNotEqualTo(contactPersonDTO2);
    }
}
