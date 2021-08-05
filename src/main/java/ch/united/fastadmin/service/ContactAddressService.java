package ch.united.fastadmin.service;

import ch.united.fastadmin.service.dto.ContactAddressDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ch.united.fastadmin.domain.ContactAddress}.
 */
public interface ContactAddressService {
    /**
     * Save a contactAddress.
     *
     * @param contactAddressDTO the entity to save.
     * @return the persisted entity.
     */
    ContactAddressDTO save(ContactAddressDTO contactAddressDTO);

    /**
     * Partially updates a contactAddress.
     *
     * @param contactAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContactAddressDTO> partialUpdate(ContactAddressDTO contactAddressDTO);

    /**
     * Get all the contactAddresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContactAddressDTO> findAll(Pageable pageable);

    /**
     * Get the "id" contactAddress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactAddressDTO> findOne(Long id);

    /**
     * Delete the "id" contactAddress.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contactAddress corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContactAddressDTO> search(String query, Pageable pageable);
}
