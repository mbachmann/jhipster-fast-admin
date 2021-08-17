package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CostUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostUnit.class);
        CostUnit costUnit1 = new CostUnit();
        costUnit1.setId(1L);
        CostUnit costUnit2 = new CostUnit();
        costUnit2.setId(costUnit1.getId());
        assertThat(costUnit1).isEqualTo(costUnit2);
        costUnit2.setId(2L);
        assertThat(costUnit1).isNotEqualTo(costUnit2);
        costUnit1.setId(null);
        assertThat(costUnit1).isNotEqualTo(costUnit2);
    }
}
