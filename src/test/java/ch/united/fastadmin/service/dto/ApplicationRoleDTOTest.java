package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationRoleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationRoleDTO.class);
        ApplicationRoleDTO applicationRoleDTO1 = new ApplicationRoleDTO();
        applicationRoleDTO1.setId(1L);
        ApplicationRoleDTO applicationRoleDTO2 = new ApplicationRoleDTO();
        assertThat(applicationRoleDTO1).isNotEqualTo(applicationRoleDTO2);
        applicationRoleDTO2.setId(applicationRoleDTO1.getId());
        assertThat(applicationRoleDTO1).isEqualTo(applicationRoleDTO2);
        applicationRoleDTO2.setId(2L);
        assertThat(applicationRoleDTO1).isNotEqualTo(applicationRoleDTO2);
        applicationRoleDTO1.setId(null);
        assertThat(applicationRoleDTO1).isNotEqualTo(applicationRoleDTO2);
    }
}
