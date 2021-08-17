package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogUnitMapperTest {

    private CatalogUnitMapper catalogUnitMapper;

    @BeforeEach
    public void setUp() {
        catalogUnitMapper = new CatalogUnitMapperImpl();
    }
}
