package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.Isr;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Isr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IsrRepository extends JpaRepository<Isr, Long> {}
