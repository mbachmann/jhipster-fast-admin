package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValueAddedTaxTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValueAddedTax.class);
        ValueAddedTax valueAddedTax1 = new ValueAddedTax();
        valueAddedTax1.setId(1L);
        ValueAddedTax valueAddedTax2 = new ValueAddedTax();
        valueAddedTax2.setId(valueAddedTax1.getId());
        assertThat(valueAddedTax1).isEqualTo(valueAddedTax2);
        valueAddedTax2.setId(2L);
        assertThat(valueAddedTax1).isNotEqualTo(valueAddedTax2);
        valueAddedTax1.setId(null);
        assertThat(valueAddedTax1).isNotEqualTo(valueAddedTax2);
    }
}
