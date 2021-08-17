package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderConfirmationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderConfirmationDTO.class);
        OrderConfirmationDTO orderConfirmationDTO1 = new OrderConfirmationDTO();
        orderConfirmationDTO1.setId(1L);
        OrderConfirmationDTO orderConfirmationDTO2 = new OrderConfirmationDTO();
        assertThat(orderConfirmationDTO1).isNotEqualTo(orderConfirmationDTO2);
        orderConfirmationDTO2.setId(orderConfirmationDTO1.getId());
        assertThat(orderConfirmationDTO1).isEqualTo(orderConfirmationDTO2);
        orderConfirmationDTO2.setId(2L);
        assertThat(orderConfirmationDTO1).isNotEqualTo(orderConfirmationDTO2);
        orderConfirmationDTO1.setId(null);
        assertThat(orderConfirmationDTO1).isNotEqualTo(orderConfirmationDTO2);
    }
}
