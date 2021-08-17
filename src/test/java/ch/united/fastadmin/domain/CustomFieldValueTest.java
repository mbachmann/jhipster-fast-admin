package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomFieldValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomFieldValue.class);
        CustomFieldValue customFieldValue1 = new CustomFieldValue();
        customFieldValue1.setId(1L);
        CustomFieldValue customFieldValue2 = new CustomFieldValue();
        customFieldValue2.setId(customFieldValue1.getId());
        assertThat(customFieldValue1).isEqualTo(customFieldValue2);
        customFieldValue2.setId(2L);
        assertThat(customFieldValue1).isNotEqualTo(customFieldValue2);
        customFieldValue1.setId(null);
        assertThat(customFieldValue1).isNotEqualTo(customFieldValue2);
    }
}
