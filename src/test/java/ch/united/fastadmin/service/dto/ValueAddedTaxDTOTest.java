package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValueAddedTaxDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValueAddedTaxDTO.class);
        ValueAddedTaxDTO valueAddedTaxDTO1 = new ValueAddedTaxDTO();
        valueAddedTaxDTO1.setId(1L);
        ValueAddedTaxDTO valueAddedTaxDTO2 = new ValueAddedTaxDTO();
        assertThat(valueAddedTaxDTO1).isNotEqualTo(valueAddedTaxDTO2);
        valueAddedTaxDTO2.setId(valueAddedTaxDTO1.getId());
        assertThat(valueAddedTaxDTO1).isEqualTo(valueAddedTaxDTO2);
        valueAddedTaxDTO2.setId(2L);
        assertThat(valueAddedTaxDTO1).isNotEqualTo(valueAddedTaxDTO2);
        valueAddedTaxDTO1.setId(null);
        assertThat(valueAddedTaxDTO1).isNotEqualTo(valueAddedTaxDTO2);
    }
}
