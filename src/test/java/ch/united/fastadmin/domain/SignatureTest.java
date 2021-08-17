package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SignatureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Signature.class);
        Signature signature1 = new Signature();
        signature1.setId(1L);
        Signature signature2 = new Signature();
        signature2.setId(signature1.getId());
        assertThat(signature1).isEqualTo(signature2);
        signature2.setId(2L);
        assertThat(signature1).isNotEqualTo(signature2);
        signature1.setId(null);
        assertThat(signature1).isNotEqualTo(signature2);
    }
}
