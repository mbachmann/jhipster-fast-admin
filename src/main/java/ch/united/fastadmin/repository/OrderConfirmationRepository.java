package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.OrderConfirmation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderConfirmation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderConfirmationRepository extends JpaRepository<OrderConfirmation, Long>, JpaSpecificationExecutor<OrderConfirmation> {}
