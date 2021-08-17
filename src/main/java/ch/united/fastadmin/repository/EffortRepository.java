package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.Effort;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Effort entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EffortRepository extends JpaRepository<Effort, Long> {}
