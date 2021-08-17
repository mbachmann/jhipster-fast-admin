package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationRole.class);
        ApplicationRole applicationRole1 = new ApplicationRole();
        applicationRole1.setId(1L);
        ApplicationRole applicationRole2 = new ApplicationRole();
        applicationRole2.setId(applicationRole1.getId());
        assertThat(applicationRole1).isEqualTo(applicationRole2);
        applicationRole2.setId(2L);
        assertThat(applicationRole1).isNotEqualTo(applicationRole2);
        applicationRole1.setId(null);
        assertThat(applicationRole1).isNotEqualTo(applicationRole2);
    }
}
