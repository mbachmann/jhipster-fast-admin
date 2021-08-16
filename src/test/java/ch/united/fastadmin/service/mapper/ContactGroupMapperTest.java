package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactGroupMapperTest {

    private ContactGroupMapper contactGroupMapper;

    @BeforeEach
    public void setUp() {
        contactGroupMapper = new ContactGroupMapperImpl();
    }
}
