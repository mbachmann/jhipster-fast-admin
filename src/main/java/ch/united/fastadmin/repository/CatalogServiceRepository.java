package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.CatalogService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CatalogService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogServiceRepository extends JpaRepository<CatalogService, Long>, JpaSpecificationExecutor<CatalogService> {}
