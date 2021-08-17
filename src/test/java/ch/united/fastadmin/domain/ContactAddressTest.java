package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactAddress.class);
        ContactAddress contactAddress1 = new ContactAddress();
        contactAddress1.setId(1L);
        ContactAddress contactAddress2 = new ContactAddress();
        contactAddress2.setId(contactAddress1.getId());
        assertThat(contactAddress1).isEqualTo(contactAddress2);
        contactAddress2.setId(2L);
        assertThat(contactAddress1).isNotEqualTo(contactAddress2);
        contactAddress1.setId(null);
        assertThat(contactAddress1).isNotEqualTo(contactAddress2);
    }
}
