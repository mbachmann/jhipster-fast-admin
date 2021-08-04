package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomFieldMapperTest {

    private CustomFieldMapper customFieldMapper;

    @BeforeEach
    public void setUp() {
        customFieldMapper = new CustomFieldMapperImpl();
    }
}
