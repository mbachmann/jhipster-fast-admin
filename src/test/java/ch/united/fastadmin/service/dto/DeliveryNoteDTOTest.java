package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryNoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryNoteDTO.class);
        DeliveryNoteDTO deliveryNoteDTO1 = new DeliveryNoteDTO();
        deliveryNoteDTO1.setId(1L);
        DeliveryNoteDTO deliveryNoteDTO2 = new DeliveryNoteDTO();
        assertThat(deliveryNoteDTO1).isNotEqualTo(deliveryNoteDTO2);
        deliveryNoteDTO2.setId(deliveryNoteDTO1.getId());
        assertThat(deliveryNoteDTO1).isEqualTo(deliveryNoteDTO2);
        deliveryNoteDTO2.setId(2L);
        assertThat(deliveryNoteDTO1).isNotEqualTo(deliveryNoteDTO2);
        deliveryNoteDTO1.setId(null);
        assertThat(deliveryNoteDTO1).isNotEqualTo(deliveryNoteDTO2);
    }
}
