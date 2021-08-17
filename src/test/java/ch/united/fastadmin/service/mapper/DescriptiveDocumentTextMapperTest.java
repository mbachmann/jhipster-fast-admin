package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DescriptiveDocumentTextMapperTest {

    private DescriptiveDocumentTextMapper descriptiveDocumentTextMapper;

    @BeforeEach
    public void setUp() {
        descriptiveDocumentTextMapper = new DescriptiveDocumentTextMapperImpl();
    }
}
