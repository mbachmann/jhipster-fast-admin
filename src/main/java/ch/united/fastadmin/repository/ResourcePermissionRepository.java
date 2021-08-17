package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ResourcePermission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ResourcePermission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourcePermissionRepository extends JpaRepository<ResourcePermission, Long> {}
