package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ApplicationRole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationRoleRepository extends JpaRepository<ApplicationRole, Long> {}
