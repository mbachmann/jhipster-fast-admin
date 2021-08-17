package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExchangeRateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExchangeRate.class);
        ExchangeRate exchangeRate1 = new ExchangeRate();
        exchangeRate1.setId(1L);
        ExchangeRate exchangeRate2 = new ExchangeRate();
        exchangeRate2.setId(exchangeRate1.getId());
        assertThat(exchangeRate1).isEqualTo(exchangeRate2);
        exchangeRate2.setId(2L);
        assertThat(exchangeRate1).isNotEqualTo(exchangeRate2);
        exchangeRate1.setId(null);
        assertThat(exchangeRate1).isNotEqualTo(exchangeRate2);
    }
}
