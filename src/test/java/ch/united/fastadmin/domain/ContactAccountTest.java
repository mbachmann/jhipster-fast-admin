package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactAccount.class);
        ContactAccount contactAccount1 = new ContactAccount();
        contactAccount1.setId(1L);
        ContactAccount contactAccount2 = new ContactAccount();
        contactAccount2.setId(contactAccount1.getId());
        assertThat(contactAccount1).isEqualTo(contactAccount2);
        contactAccount2.setId(2L);
        assertThat(contactAccount1).isNotEqualTo(contactAccount2);
        contactAccount1.setId(null);
        assertThat(contactAccount1).isNotEqualTo(contactAccount2);
    }
}
