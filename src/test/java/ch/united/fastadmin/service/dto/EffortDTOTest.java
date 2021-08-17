package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EffortDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EffortDTO.class);
        EffortDTO effortDTO1 = new EffortDTO();
        effortDTO1.setId(1L);
        EffortDTO effortDTO2 = new EffortDTO();
        assertThat(effortDTO1).isNotEqualTo(effortDTO2);
        effortDTO2.setId(effortDTO1.getId());
        assertThat(effortDTO1).isEqualTo(effortDTO2);
        effortDTO2.setId(2L);
        assertThat(effortDTO1).isNotEqualTo(effortDTO2);
        effortDTO1.setId(null);
        assertThat(effortDTO1).isNotEqualTo(effortDTO2);
    }
}
