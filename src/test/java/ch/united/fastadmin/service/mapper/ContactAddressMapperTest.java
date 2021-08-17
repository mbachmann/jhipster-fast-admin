package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactAddressMapperTest {

    private ContactAddressMapper contactAddressMapper;

    @BeforeEach
    public void setUp() {
        contactAddressMapper = new ContactAddressMapperImpl();
    }
}
