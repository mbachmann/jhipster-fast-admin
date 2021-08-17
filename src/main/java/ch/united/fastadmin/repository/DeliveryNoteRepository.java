package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.DeliveryNote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeliveryNote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, Long> {}
