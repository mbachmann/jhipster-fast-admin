package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentTextMapperTest {

    private DocumentTextMapper documentTextMapper;

    @BeforeEach
    public void setUp() {
        documentTextMapper = new DocumentTextMapperImpl();
    }
}
