package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ContactGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContactGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactGroupRepository extends JpaRepository<ContactGroup, Long> {}
