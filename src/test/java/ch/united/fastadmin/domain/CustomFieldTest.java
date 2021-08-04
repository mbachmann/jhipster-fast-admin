package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomFieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomField.class);
        CustomField customField1 = new CustomField();
        customField1.setId(1L);
        CustomField customField2 = new CustomField();
        customField2.setId(customField1.getId());
        assertThat(customField1).isEqualTo(customField2);
        customField2.setId(2L);
        assertThat(customField1).isNotEqualTo(customField2);
        customField1.setId(null);
        assertThat(customField1).isNotEqualTo(customField2);
    }
}
