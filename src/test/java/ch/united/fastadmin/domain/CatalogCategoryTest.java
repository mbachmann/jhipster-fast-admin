package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogCategory.class);
        CatalogCategory catalogCategory1 = new CatalogCategory();
        catalogCategory1.setId(1L);
        CatalogCategory catalogCategory2 = new CatalogCategory();
        catalogCategory2.setId(catalogCategory1.getId());
        assertThat(catalogCategory1).isEqualTo(catalogCategory2);
        catalogCategory2.setId(2L);
        assertThat(catalogCategory1).isNotEqualTo(catalogCategory2);
        catalogCategory1.setId(null);
        assertThat(catalogCategory1).isNotEqualTo(catalogCategory2);
    }
}
