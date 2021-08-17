package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.FreeText;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FreeText entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FreeTextRepository extends JpaRepository<FreeText, Long> {}
