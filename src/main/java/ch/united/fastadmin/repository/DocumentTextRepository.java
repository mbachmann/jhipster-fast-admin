package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.DocumentText;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentText entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentTextRepository extends JpaRepository<DocumentText, Long> {}
