package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactGroup.class);
        ContactGroup contactGroup1 = new ContactGroup();
        contactGroup1.setId(1L);
        ContactGroup contactGroup2 = new ContactGroup();
        contactGroup2.setId(contactGroup1.getId());
        assertThat(contactGroup1).isEqualTo(contactGroup2);
        contactGroup2.setId(2L);
        assertThat(contactGroup1).isNotEqualTo(contactGroup2);
        contactGroup1.setId(null);
        assertThat(contactGroup1).isNotEqualTo(contactGroup2);
    }
}
