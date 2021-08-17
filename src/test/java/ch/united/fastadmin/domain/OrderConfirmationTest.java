package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderConfirmationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderConfirmation.class);
        OrderConfirmation orderConfirmation1 = new OrderConfirmation();
        orderConfirmation1.setId(1L);
        OrderConfirmation orderConfirmation2 = new OrderConfirmation();
        orderConfirmation2.setId(orderConfirmation1.getId());
        assertThat(orderConfirmation1).isEqualTo(orderConfirmation2);
        orderConfirmation2.setId(2L);
        assertThat(orderConfirmation1).isNotEqualTo(orderConfirmation2);
        orderConfirmation1.setId(null);
        assertThat(orderConfirmation1).isNotEqualTo(orderConfirmation2);
    }
}
