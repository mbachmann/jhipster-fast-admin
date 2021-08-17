package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.DescriptiveDocumentText;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DescriptiveDocumentText entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescriptiveDocumentTextRepository extends JpaRepository<DescriptiveDocumentText, Long> {}
