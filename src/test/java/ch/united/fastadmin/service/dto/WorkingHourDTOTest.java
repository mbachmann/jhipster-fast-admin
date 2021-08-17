package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkingHourDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkingHourDTO.class);
        WorkingHourDTO workingHourDTO1 = new WorkingHourDTO();
        workingHourDTO1.setId(1L);
        WorkingHourDTO workingHourDTO2 = new WorkingHourDTO();
        assertThat(workingHourDTO1).isNotEqualTo(workingHourDTO2);
        workingHourDTO2.setId(workingHourDTO1.getId());
        assertThat(workingHourDTO1).isEqualTo(workingHourDTO2);
        workingHourDTO2.setId(2L);
        assertThat(workingHourDTO1).isNotEqualTo(workingHourDTO2);
        workingHourDTO1.setId(null);
        assertThat(workingHourDTO1).isNotEqualTo(workingHourDTO2);
    }
}
