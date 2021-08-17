package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogUnit.class);
        CatalogUnit catalogUnit1 = new CatalogUnit();
        catalogUnit1.setId(1L);
        CatalogUnit catalogUnit2 = new CatalogUnit();
        catalogUnit2.setId(catalogUnit1.getId());
        assertThat(catalogUnit1).isEqualTo(catalogUnit2);
        catalogUnit2.setId(2L);
        assertThat(catalogUnit1).isNotEqualTo(catalogUnit2);
        catalogUnit1.setId(null);
        assertThat(catalogUnit1).isNotEqualTo(catalogUnit2);
    }
}
