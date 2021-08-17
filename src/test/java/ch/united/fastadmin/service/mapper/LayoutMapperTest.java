package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LayoutMapperTest {

    private LayoutMapper layoutMapper;

    @BeforeEach
    public void setUp() {
        layoutMapper = new LayoutMapperImpl();
    }
}
