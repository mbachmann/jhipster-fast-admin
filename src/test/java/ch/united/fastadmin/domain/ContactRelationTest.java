package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactRelationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactRelation.class);
        ContactRelation contactRelation1 = new ContactRelation();
        contactRelation1.setId(1L);
        ContactRelation contactRelation2 = new ContactRelation();
        contactRelation2.setId(contactRelation1.getId());
        assertThat(contactRelation1).isEqualTo(contactRelation2);
        contactRelation2.setId(2L);
        assertThat(contactRelation1).isNotEqualTo(contactRelation2);
        contactRelation1.setId(null);
        assertThat(contactRelation1).isNotEqualTo(contactRelation2);
    }
}
