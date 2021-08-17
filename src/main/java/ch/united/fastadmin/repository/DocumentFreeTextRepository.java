package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.DocumentFreeText;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentFreeText entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentFreeTextRepository extends JpaRepository<DocumentFreeText, Long> {}
