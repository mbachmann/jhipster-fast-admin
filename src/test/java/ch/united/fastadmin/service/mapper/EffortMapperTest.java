package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EffortMapperTest {

    private EffortMapper effortMapper;

    @BeforeEach
    public void setUp() {
        effortMapper = new EffortMapperImpl();
    }
}
