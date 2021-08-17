package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactAddressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactAddressDTO.class);
        ContactAddressDTO contactAddressDTO1 = new ContactAddressDTO();
        contactAddressDTO1.setId(1L);
        ContactAddressDTO contactAddressDTO2 = new ContactAddressDTO();
        assertThat(contactAddressDTO1).isNotEqualTo(contactAddressDTO2);
        contactAddressDTO2.setId(contactAddressDTO1.getId());
        assertThat(contactAddressDTO1).isEqualTo(contactAddressDTO2);
        contactAddressDTO2.setId(2L);
        assertThat(contactAddressDTO1).isNotEqualTo(contactAddressDTO2);
        contactAddressDTO1.setId(null);
        assertThat(contactAddressDTO1).isNotEqualTo(contactAddressDTO2);
    }
}
