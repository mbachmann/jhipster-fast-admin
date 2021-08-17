package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ReportingActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReportingActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportingActivityRepository extends JpaRepository<ReportingActivity, Long> {}
