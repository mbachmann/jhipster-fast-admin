package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.Layout;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Layout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LayoutRepository extends JpaRepository<Layout, Long> {}
