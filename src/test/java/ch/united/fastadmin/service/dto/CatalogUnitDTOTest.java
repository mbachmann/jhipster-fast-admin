package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogUnitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogUnitDTO.class);
        CatalogUnitDTO catalogUnitDTO1 = new CatalogUnitDTO();
        catalogUnitDTO1.setId(1L);
        CatalogUnitDTO catalogUnitDTO2 = new CatalogUnitDTO();
        assertThat(catalogUnitDTO1).isNotEqualTo(catalogUnitDTO2);
        catalogUnitDTO2.setId(catalogUnitDTO1.getId());
        assertThat(catalogUnitDTO1).isEqualTo(catalogUnitDTO2);
        catalogUnitDTO2.setId(2L);
        assertThat(catalogUnitDTO1).isNotEqualTo(catalogUnitDTO2);
        catalogUnitDTO1.setId(null);
        assertThat(catalogUnitDTO1).isNotEqualTo(catalogUnitDTO2);
    }
}
