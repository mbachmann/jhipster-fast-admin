package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ValueAddedTax;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ValueAddedTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValueAddedTaxRepository extends JpaRepository<ValueAddedTax, Long> {}
