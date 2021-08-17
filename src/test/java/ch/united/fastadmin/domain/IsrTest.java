package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IsrTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Isr.class);
        Isr isr1 = new Isr();
        isr1.setId(1L);
        Isr isr2 = new Isr();
        isr2.setId(isr1.getId());
        assertThat(isr1).isEqualTo(isr2);
        isr2.setId(2L);
        assertThat(isr1).isNotEqualTo(isr2);
        isr1.setId(null);
        assertThat(isr1).isNotEqualTo(isr2);
    }
}
