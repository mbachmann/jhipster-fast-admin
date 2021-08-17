package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IsrDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IsrDTO.class);
        IsrDTO isrDTO1 = new IsrDTO();
        isrDTO1.setId(1L);
        IsrDTO isrDTO2 = new IsrDTO();
        assertThat(isrDTO1).isNotEqualTo(isrDTO2);
        isrDTO2.setId(isrDTO1.getId());
        assertThat(isrDTO1).isEqualTo(isrDTO2);
        isrDTO2.setId(2L);
        assertThat(isrDTO1).isNotEqualTo(isrDTO2);
        isrDTO1.setId(null);
        assertThat(isrDTO1).isNotEqualTo(isrDTO2);
    }
}
