package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentFreeTextMapperTest {

    private DocumentFreeTextMapper documentFreeTextMapper;

    @BeforeEach
    public void setUp() {
        documentFreeTextMapper = new DocumentFreeTextMapperImpl();
    }
}
