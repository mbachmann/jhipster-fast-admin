package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValueAddedTaxMapperTest {

    private ValueAddedTaxMapper valueAddedTaxMapper;

    @BeforeEach
    public void setUp() {
        valueAddedTaxMapper = new ValueAddedTaxMapperImpl();
    }
}
