package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderConfirmationMapperTest {

    private OrderConfirmationMapper orderConfirmationMapper;

    @BeforeEach
    public void setUp() {
        orderConfirmationMapper = new OrderConfirmationMapperImpl();
    }
}
