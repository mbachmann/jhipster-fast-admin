package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogCategoryDTO.class);
        CatalogCategoryDTO catalogCategoryDTO1 = new CatalogCategoryDTO();
        catalogCategoryDTO1.setId(1L);
        CatalogCategoryDTO catalogCategoryDTO2 = new CatalogCategoryDTO();
        assertThat(catalogCategoryDTO1).isNotEqualTo(catalogCategoryDTO2);
        catalogCategoryDTO2.setId(catalogCategoryDTO1.getId());
        assertThat(catalogCategoryDTO1).isEqualTo(catalogCategoryDTO2);
        catalogCategoryDTO2.setId(2L);
        assertThat(catalogCategoryDTO1).isNotEqualTo(catalogCategoryDTO2);
        catalogCategoryDTO1.setId(null);
        assertThat(catalogCategoryDTO1).isNotEqualTo(catalogCategoryDTO2);
    }
}
