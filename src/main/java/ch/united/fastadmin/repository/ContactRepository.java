package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.Contact;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Contact entity.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query(
        value = "select distinct contact from Contact contact left join fetch contact.relations left join fetch contact.groups",
        countQuery = "select count(distinct contact) from Contact contact"
    )
    Page<Contact> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct contact from Contact contact left join fetch contact.relations left join fetch contact.groups")
    List<Contact> findAllWithEagerRelationships();

    @Query("select contact from Contact contact left join fetch contact.relations left join fetch contact.groups where contact.id =:id")
    Optional<Contact> findOneWithEagerRelationships(@Param("id") Long id);
}
