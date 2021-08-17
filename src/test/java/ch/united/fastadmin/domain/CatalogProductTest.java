package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogProduct.class);
        CatalogProduct catalogProduct1 = new CatalogProduct();
        catalogProduct1.setId(1L);
        CatalogProduct catalogProduct2 = new CatalogProduct();
        catalogProduct2.setId(catalogProduct1.getId());
        assertThat(catalogProduct1).isEqualTo(catalogProduct2);
        catalogProduct2.setId(2L);
        assertThat(catalogProduct1).isNotEqualTo(catalogProduct2);
        catalogProduct1.setId(null);
        assertThat(catalogProduct1).isNotEqualTo(catalogProduct2);
    }
}
