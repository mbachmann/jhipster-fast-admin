package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogServiceDTO.class);
        CatalogServiceDTO catalogServiceDTO1 = new CatalogServiceDTO();
        catalogServiceDTO1.setId(1L);
        CatalogServiceDTO catalogServiceDTO2 = new CatalogServiceDTO();
        assertThat(catalogServiceDTO1).isNotEqualTo(catalogServiceDTO2);
        catalogServiceDTO2.setId(catalogServiceDTO1.getId());
        assertThat(catalogServiceDTO1).isEqualTo(catalogServiceDTO2);
        catalogServiceDTO2.setId(2L);
        assertThat(catalogServiceDTO1).isNotEqualTo(catalogServiceDTO2);
        catalogServiceDTO1.setId(null);
        assertThat(catalogServiceDTO1).isNotEqualTo(catalogServiceDTO2);
    }
}
