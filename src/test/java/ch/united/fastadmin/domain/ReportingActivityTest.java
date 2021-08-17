package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportingActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportingActivity.class);
        ReportingActivity reportingActivity1 = new ReportingActivity();
        reportingActivity1.setId(1L);
        ReportingActivity reportingActivity2 = new ReportingActivity();
        reportingActivity2.setId(reportingActivity1.getId());
        assertThat(reportingActivity1).isEqualTo(reportingActivity2);
        reportingActivity2.setId(2L);
        assertThat(reportingActivity1).isNotEqualTo(reportingActivity2);
        reportingActivity1.setId(null);
        assertThat(reportingActivity1).isNotEqualTo(reportingActivity2);
    }
}
