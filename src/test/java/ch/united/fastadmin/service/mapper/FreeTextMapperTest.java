package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FreeTextMapperTest {

    private FreeTextMapper freeTextMapper;

    @BeforeEach
    public void setUp() {
        freeTextMapper = new FreeTextMapperImpl();
    }
}
