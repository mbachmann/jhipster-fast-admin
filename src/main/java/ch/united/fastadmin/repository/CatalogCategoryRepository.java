package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.CatalogCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CatalogCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogCategoryRepository extends JpaRepository<CatalogCategory, Long> {}
