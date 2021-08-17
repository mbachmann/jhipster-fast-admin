package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SignatureMapperTest {

    private SignatureMapper signatureMapper;

    @BeforeEach
    public void setUp() {
        signatureMapper = new SignatureMapperImpl();
    }
}
