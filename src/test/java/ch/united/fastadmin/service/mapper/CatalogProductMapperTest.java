package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogProductMapperTest {

    private CatalogProductMapper catalogProductMapper;

    @BeforeEach
    public void setUp() {
        catalogProductMapper = new CatalogProductMapperImpl();
    }
}
