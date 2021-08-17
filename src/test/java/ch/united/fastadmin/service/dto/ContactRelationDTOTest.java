package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactRelationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactRelationDTO.class);
        ContactRelationDTO contactRelationDTO1 = new ContactRelationDTO();
        contactRelationDTO1.setId(1L);
        ContactRelationDTO contactRelationDTO2 = new ContactRelationDTO();
        assertThat(contactRelationDTO1).isNotEqualTo(contactRelationDTO2);
        contactRelationDTO2.setId(contactRelationDTO1.getId());
        assertThat(contactRelationDTO1).isEqualTo(contactRelationDTO2);
        contactRelationDTO2.setId(2L);
        assertThat(contactRelationDTO1).isNotEqualTo(contactRelationDTO2);
        contactRelationDTO1.setId(null);
        assertThat(contactRelationDTO1).isNotEqualTo(contactRelationDTO2);
    }
}
