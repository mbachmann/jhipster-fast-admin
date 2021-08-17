package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourcePermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourcePermission.class);
        ResourcePermission resourcePermission1 = new ResourcePermission();
        resourcePermission1.setId(1L);
        ResourcePermission resourcePermission2 = new ResourcePermission();
        resourcePermission2.setId(resourcePermission1.getId());
        assertThat(resourcePermission1).isEqualTo(resourcePermission2);
        resourcePermission2.setId(2L);
        assertThat(resourcePermission1).isNotEqualTo(resourcePermission2);
        resourcePermission1.setId(null);
        assertThat(resourcePermission1).isNotEqualTo(resourcePermission2);
    }
}
