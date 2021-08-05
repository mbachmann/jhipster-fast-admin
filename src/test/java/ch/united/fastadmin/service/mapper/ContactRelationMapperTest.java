package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactRelationMapperTest {

    private ContactRelationMapper contactRelationMapper;

    @BeforeEach
    public void setUp() {
        contactRelationMapper = new ContactRelationMapperImpl();
    }
}
