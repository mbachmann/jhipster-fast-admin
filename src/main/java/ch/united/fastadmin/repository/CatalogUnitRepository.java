package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.CatalogUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CatalogUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogUnitRepository extends JpaRepository<CatalogUnit, Long> {}
