package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FreeTextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FreeTextDTO.class);
        FreeTextDTO freeTextDTO1 = new FreeTextDTO();
        freeTextDTO1.setId(1L);
        FreeTextDTO freeTextDTO2 = new FreeTextDTO();
        assertThat(freeTextDTO1).isNotEqualTo(freeTextDTO2);
        freeTextDTO2.setId(freeTextDTO1.getId());
        assertThat(freeTextDTO1).isEqualTo(freeTextDTO2);
        freeTextDTO2.setId(2L);
        assertThat(freeTextDTO1).isNotEqualTo(freeTextDTO2);
        freeTextDTO1.setId(null);
        assertThat(freeTextDTO1).isNotEqualTo(freeTextDTO2);
    }
}
