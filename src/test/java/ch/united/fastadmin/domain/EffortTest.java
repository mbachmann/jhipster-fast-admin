package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EffortTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Effort.class);
        Effort effort1 = new Effort();
        effort1.setId(1L);
        Effort effort2 = new Effort();
        effort2.setId(effort1.getId());
        assertThat(effort1).isEqualTo(effort2);
        effort2.setId(2L);
        assertThat(effort1).isNotEqualTo(effort2);
        effort1.setId(null);
        assertThat(effort1).isNotEqualTo(effort2);
    }
}
