package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportingActivityMapperTest {

    private ReportingActivityMapper reportingActivityMapper;

    @BeforeEach
    public void setUp() {
        reportingActivityMapper = new ReportingActivityMapperImpl();
    }
}
