package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FreeTextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FreeText.class);
        FreeText freeText1 = new FreeText();
        freeText1.setId(1L);
        FreeText freeText2 = new FreeText();
        freeText2.setId(freeText1.getId());
        assertThat(freeText1).isEqualTo(freeText2);
        freeText2.setId(2L);
        assertThat(freeText1).isNotEqualTo(freeText2);
        freeText1.setId(null);
        assertThat(freeText1).isNotEqualTo(freeText2);
    }
}
