package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.CustomFieldValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CustomFieldValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomFieldValueRepository extends JpaRepository<CustomFieldValue, Long> {}
