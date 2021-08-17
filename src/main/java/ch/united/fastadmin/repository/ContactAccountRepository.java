package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ContactAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContactAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactAccountRepository extends JpaRepository<ContactAccount, Long> {}
