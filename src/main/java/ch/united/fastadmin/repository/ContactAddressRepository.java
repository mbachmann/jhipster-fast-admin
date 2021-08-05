package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ContactAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContactAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactAddressRepository extends JpaRepository<ContactAddress, Long> {}
