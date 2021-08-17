package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.WorkingHour;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkingHour entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkingHourRepository extends JpaRepository<WorkingHour, Long> {}
