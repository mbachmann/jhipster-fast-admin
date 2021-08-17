package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingActivityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingActivityDTO.class);
        ReportingActivityDTO reportingActivityDTO1 = new ReportingActivityDTO();
        reportingActivityDTO1.setId(1L);
        ReportingActivityDTO reportingActivityDTO2 = new ReportingActivityDTO();
        assertThat(reportingActivityDTO1).isNotEqualTo(reportingActivityDTO2);
        reportingActivityDTO2.setId(reportingActivityDTO1.getId());
        assertThat(reportingActivityDTO1).isEqualTo(reportingActivityDTO2);
        reportingActivityDTO2.setId(2L);
        assertThat(reportingActivityDTO1).isNotEqualTo(reportingActivityDTO2);
        reportingActivityDTO1.setId(null);
        assertThat(reportingActivityDTO1).isNotEqualTo(reportingActivityDTO2);
    }
}
