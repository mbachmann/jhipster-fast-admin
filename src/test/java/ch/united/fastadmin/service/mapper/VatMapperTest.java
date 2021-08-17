package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VatMapperTest {

    private VatMapper vatMapper;

    @BeforeEach
    public void setUp() {
        vatMapper = new VatMapperImpl();
    }
}
