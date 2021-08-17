package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourcePermissionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourcePermissionDTO.class);
        ResourcePermissionDTO resourcePermissionDTO1 = new ResourcePermissionDTO();
        resourcePermissionDTO1.setId(1L);
        ResourcePermissionDTO resourcePermissionDTO2 = new ResourcePermissionDTO();
        assertThat(resourcePermissionDTO1).isNotEqualTo(resourcePermissionDTO2);
        resourcePermissionDTO2.setId(resourcePermissionDTO1.getId());
        assertThat(resourcePermissionDTO1).isEqualTo(resourcePermissionDTO2);
        resourcePermissionDTO2.setId(2L);
        assertThat(resourcePermissionDTO1).isNotEqualTo(resourcePermissionDTO2);
        resourcePermissionDTO1.setId(null);
        assertThat(resourcePermissionDTO1).isNotEqualTo(resourcePermissionDTO2);
    }
}
