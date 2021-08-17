package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CostUnitMapperTest {

    private CostUnitMapper costUnitMapper;

    @BeforeEach
    public void setUp() {
        costUnitMapper = new CostUnitMapperImpl();
    }
}
