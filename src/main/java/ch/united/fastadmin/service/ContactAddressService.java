package ch.united.fastadmin.service;

import ch.united.fastadmin.service.dto.ContactAddressDTO;
import java.util.List;
import java.util.Optional;

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
     * @return the list of entities.
     */
    List<ContactAddressDTO> findAll();

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
     * @return the list of entities.
     */
    List<ContactAddressDTO> search(String query);
}
