package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.CustomField;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CustomField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomFieldRepository extends JpaRepository<CustomField, Long>, JpaSpecificationExecutor<CustomField> {}
