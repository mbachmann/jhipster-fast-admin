package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.Signature;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Signature entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignatureRepository extends JpaRepository<Signature, Long> {}
