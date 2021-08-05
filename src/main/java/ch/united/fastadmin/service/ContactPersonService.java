package ch.united.fastadmin.service;

import ch.united.fastadmin.service.dto.ContactPersonDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ch.united.fastadmin.domain.ContactPerson}.
 */
public interface ContactPersonService {
    /**
     * Save a contactPerson.
     *
     * @param contactPersonDTO the entity to save.
     * @return the persisted entity.
     */
    ContactPersonDTO save(ContactPersonDTO contactPersonDTO);

    /**
     * Partially updates a contactPerson.
     *
     * @param contactPersonDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContactPersonDTO> partialUpdate(ContactPersonDTO contactPersonDTO);

    /**
     * Get all the contactPeople.
     *
     * @return the list of entities.
     */
    List<ContactPersonDTO> findAll();

    /**
     * Get the "id" contactPerson.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactPersonDTO> findOne(Long id);

    /**
     * Delete the "id" contactPerson.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contactPerson corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<ContactPersonDTO> search(String query);
}
