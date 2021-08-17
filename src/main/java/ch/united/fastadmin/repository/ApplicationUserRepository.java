package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ApplicationUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {}
