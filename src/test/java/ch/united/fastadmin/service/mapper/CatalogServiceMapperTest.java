package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogServiceMapperTest {

    private CatalogServiceMapper catalogServiceMapper;

    @BeforeEach
    public void setUp() {
        catalogServiceMapper = new CatalogServiceMapperImpl();
    }
}
