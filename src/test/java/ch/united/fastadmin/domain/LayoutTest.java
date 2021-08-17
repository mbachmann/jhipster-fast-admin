package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LayoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Layout.class);
        Layout layout1 = new Layout();
        layout1.setId(1L);
        Layout layout2 = new Layout();
        layout2.setId(layout1.getId());
        assertThat(layout1).isEqualTo(layout2);
        layout2.setId(2L);
        assertThat(layout1).isNotEqualTo(layout2);
        layout1.setId(null);
        assertThat(layout1).isNotEqualTo(layout2);
    }
}
