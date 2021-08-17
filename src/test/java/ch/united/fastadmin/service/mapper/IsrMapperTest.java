package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IsrMapperTest {

    private IsrMapper isrMapper;

    @BeforeEach
    public void setUp() {
        isrMapper = new IsrMapperImpl();
    }
}
