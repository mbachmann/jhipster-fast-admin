package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.ContactRelation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContactRelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRelationRepository extends JpaRepository<ContactRelation, Long>, JpaSpecificationExecutor<ContactRelation> {}
