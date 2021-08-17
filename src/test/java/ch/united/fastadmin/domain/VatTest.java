package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vat.class);
        Vat vat1 = new Vat();
        vat1.setId(1L);
        Vat vat2 = new Vat();
        vat2.setId(vat1.getId());
        assertThat(vat1).isEqualTo(vat2);
        vat2.setId(2L);
        assertThat(vat1).isNotEqualTo(vat2);
        vat1.setId(null);
        assertThat(vat1).isNotEqualTo(vat2);
    }
}
