package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogProductDTO.class);
        CatalogProductDTO catalogProductDTO1 = new CatalogProductDTO();
        catalogProductDTO1.setId(1L);
        CatalogProductDTO catalogProductDTO2 = new CatalogProductDTO();
        assertThat(catalogProductDTO1).isNotEqualTo(catalogProductDTO2);
        catalogProductDTO2.setId(catalogProductDTO1.getId());
        assertThat(catalogProductDTO1).isEqualTo(catalogProductDTO2);
        catalogProductDTO2.setId(2L);
        assertThat(catalogProductDTO1).isNotEqualTo(catalogProductDTO2);
        catalogProductDTO1.setId(null);
        assertThat(catalogProductDTO1).isNotEqualTo(catalogProductDTO2);
    }
}
