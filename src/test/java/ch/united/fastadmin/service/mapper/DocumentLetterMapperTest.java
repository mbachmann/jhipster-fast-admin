package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentLetterMapperTest {

    private DocumentLetterMapper documentLetterMapper;

    @BeforeEach
    public void setUp() {
        documentLetterMapper = new DocumentLetterMapperImpl();
    }
}
