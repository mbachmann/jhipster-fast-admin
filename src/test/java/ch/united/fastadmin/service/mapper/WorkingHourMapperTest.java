package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkingHourMapperTest {

    private WorkingHourMapper workingHourMapper;

    @BeforeEach
    public void setUp() {
        workingHourMapper = new WorkingHourMapperImpl();
    }
}
