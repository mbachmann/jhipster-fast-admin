package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogService.class);
        CatalogService catalogService1 = new CatalogService();
        catalogService1.setId(1L);
        CatalogService catalogService2 = new CatalogService();
        catalogService2.setId(catalogService1.getId());
        assertThat(catalogService1).isEqualTo(catalogService2);
        catalogService2.setId(2L);
        assertThat(catalogService1).isNotEqualTo(catalogService2);
        catalogService1.setId(null);
        assertThat(catalogService1).isNotEqualTo(catalogService2);
    }
}
