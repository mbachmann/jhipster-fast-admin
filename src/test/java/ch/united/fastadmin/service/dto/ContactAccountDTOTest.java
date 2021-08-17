package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactAccountDTO.class);
        ContactAccountDTO contactAccountDTO1 = new ContactAccountDTO();
        contactAccountDTO1.setId(1L);
        ContactAccountDTO contactAccountDTO2 = new ContactAccountDTO();
        assertThat(contactAccountDTO1).isNotEqualTo(contactAccountDTO2);
        contactAccountDTO2.setId(contactAccountDTO1.getId());
        assertThat(contactAccountDTO1).isEqualTo(contactAccountDTO2);
        contactAccountDTO2.setId(2L);
        assertThat(contactAccountDTO1).isNotEqualTo(contactAccountDTO2);
        contactAccountDTO1.setId(null);
        assertThat(contactAccountDTO1).isNotEqualTo(contactAccountDTO2);
    }
}
