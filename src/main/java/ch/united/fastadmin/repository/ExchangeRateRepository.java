package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ExchangeRate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExchangeRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {}
