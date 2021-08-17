package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ContactReminder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContactReminder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactReminderRepository extends JpaRepository<ContactReminder, Long>, JpaSpecificationExecutor<ContactReminder> {}
