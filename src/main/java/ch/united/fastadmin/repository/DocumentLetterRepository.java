package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.DocumentLetter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentLetter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentLetterRepository extends JpaRepository<DocumentLetter, Long>, JpaSpecificationExecutor<DocumentLetter> {}
