package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomFieldValueMapperTest {

    private CustomFieldValueMapper customFieldValueMapper;

    @BeforeEach
    public void setUp() {
        customFieldValueMapper = new CustomFieldValueMapperImpl();
    }
}
