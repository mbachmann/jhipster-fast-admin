package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.Vat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VatRepository extends JpaRepository<Vat, Long> {}
