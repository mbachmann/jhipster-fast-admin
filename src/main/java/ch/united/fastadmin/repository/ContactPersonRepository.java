package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ContactPerson;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContactPerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {}
