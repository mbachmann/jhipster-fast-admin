package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourcePermissionMapperTest {

    private ResourcePermissionMapper resourcePermissionMapper;

    @BeforeEach
    public void setUp() {
        resourcePermissionMapper = new ResourcePermissionMapperImpl();
    }
}
