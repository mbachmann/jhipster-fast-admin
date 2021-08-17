package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentPositionMapperTest {

    private DocumentPositionMapper documentPositionMapper;

    @BeforeEach
    public void setUp() {
        documentPositionMapper = new DocumentPositionMapperImpl();
    }
}
