package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogCategoryMapperTest {

    private CatalogCategoryMapper catalogCategoryMapper;

    @BeforeEach
    public void setUp() {
        catalogCategoryMapper = new CatalogCategoryMapperImpl();
    }
}
