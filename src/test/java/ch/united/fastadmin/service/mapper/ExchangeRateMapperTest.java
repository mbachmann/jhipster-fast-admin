package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExchangeRateMapperTest {

    private ExchangeRateMapper exchangeRateMapper;

    @BeforeEach
    public void setUp() {
        exchangeRateMapper = new ExchangeRateMapperImpl();
    }
}
