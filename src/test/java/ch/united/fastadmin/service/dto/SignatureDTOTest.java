package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SignatureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignatureDTO.class);
        SignatureDTO signatureDTO1 = new SignatureDTO();
        signatureDTO1.setId(1L);
        SignatureDTO signatureDTO2 = new SignatureDTO();
        assertThat(signatureDTO1).isNotEqualTo(signatureDTO2);
        signatureDTO2.setId(signatureDTO1.getId());
        assertThat(signatureDTO1).isEqualTo(signatureDTO2);
        signatureDTO2.setId(2L);
        assertThat(signatureDTO1).isNotEqualTo(signatureDTO2);
        signatureDTO1.setId(null);
        assertThat(signatureDTO1).isNotEqualTo(signatureDTO2);
    }
}
