package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.DocumentPosition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentPosition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentPositionRepository extends JpaRepository<DocumentPosition, Long> {}
