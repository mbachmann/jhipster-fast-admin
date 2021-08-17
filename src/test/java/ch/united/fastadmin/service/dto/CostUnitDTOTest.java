package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CostUnitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostUnitDTO.class);
        CostUnitDTO costUnitDTO1 = new CostUnitDTO();
        costUnitDTO1.setId(1L);
        CostUnitDTO costUnitDTO2 = new CostUnitDTO();
        assertThat(costUnitDTO1).isNotEqualTo(costUnitDTO2);
        costUnitDTO2.setId(costUnitDTO1.getId());
        assertThat(costUnitDTO1).isEqualTo(costUnitDTO2);
        costUnitDTO2.setId(2L);
        assertThat(costUnitDTO1).isNotEqualTo(costUnitDTO2);
        costUnitDTO1.setId(null);
        assertThat(costUnitDTO1).isNotEqualTo(costUnitDTO2);
    }
}
