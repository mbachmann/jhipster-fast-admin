package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.CostUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CostUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CostUnitRepository extends JpaRepository<CostUnit, Long> {}
