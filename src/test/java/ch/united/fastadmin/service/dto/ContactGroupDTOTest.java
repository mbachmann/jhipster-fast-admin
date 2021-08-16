package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactGroupDTO.class);
        ContactGroupDTO contactGroupDTO1 = new ContactGroupDTO();
        contactGroupDTO1.setId(1L);
        ContactGroupDTO contactGroupDTO2 = new ContactGroupDTO();
        assertThat(contactGroupDTO1).isNotEqualTo(contactGroupDTO2);
        contactGroupDTO2.setId(contactGroupDTO1.getId());
        assertThat(contactGroupDTO1).isEqualTo(contactGroupDTO2);
        contactGroupDTO2.setId(2L);
        assertThat(contactGroupDTO1).isNotEqualTo(contactGroupDTO2);
        contactGroupDTO1.setId(null);
        assertThat(contactGroupDTO1).isNotEqualTo(contactGroupDTO2);
    }
}
