package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactReminderMapperTest {

    private ContactReminderMapper contactReminderMapper;

    @BeforeEach
    public void setUp() {
        contactReminderMapper = new ContactReminderMapperImpl();
    }
}