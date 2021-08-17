package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.CatalogProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CatalogProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogProductRepository extends JpaRepository<CatalogProduct, Long> {}
