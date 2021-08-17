package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.Offer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Offer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {}
