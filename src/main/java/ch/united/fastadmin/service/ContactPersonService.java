package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.ContactPerson;
import ch.united.fastadmin.repository.ContactPersonRepository;
import ch.united.fastadmin.service.dto.ContactPersonDTO;
import ch.united.fastadmin.service.mapper.ContactPersonMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContactPerson}.
 */
@Service
@Transactional
public class ContactPersonService {

    private final Logger log = LoggerFactory.getLogger(ContactPersonService.class);

    private final ContactPersonRepository contactPersonRepository;

    private final ContactPersonMapper contactPersonMapper;

    public ContactPersonService(ContactPersonRepository contactPersonRepository, ContactPersonMapper contactPersonMapper) {
        this.contactPersonRepository = contactPersonRepository;
        this.contactPersonMapper = contactPersonMapper;
    }

    /**
     * Save a contactPerson.
     *
     * @param contactPersonDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactPersonDTO save(ContactPersonDTO contactPersonDTO) {
        log.debug("Request to save ContactPerson : {}", contactPersonDTO);
        ContactPerson contactPerson = contactPersonMapper.toEntity(contactPersonDTO);
        contactPerson = contactPersonRepository.save(contactPerson);
        return contactPersonMapper.toDto(contactPerson);
    }

    /**
     * Partially update a contactPerson.
     *
     * @param contactPersonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactPersonDTO> partialUpdate(ContactPersonDTO contactPersonDTO) {
        log.debug("Request to partially update ContactPerson : {}", contactPersonDTO);

        return contactPersonRepository
            .findById(contactPersonDTO.getId())
            .map(
                existingContactPerson -> {
                    contactPersonMapper.partialUpdate(existingContactPerson, contactPersonDTO);

                    return existingContactPerson;
                }
            )
            .map(contactPersonRepository::save)
            .map(contactPersonMapper::toDto);
    }

    /**
     * Get all the contactPeople.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactPersonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactPeople");
        return contactPersonRepository.findAll(pageable).map(contactPersonMapper::toDto);
    }

    /**
     * Get one contactPerson by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactPersonDTO> findOne(Long id) {
        log.debug("Request to get ContactPerson : {}", id);
        return contactPersonRepository.findById(id).map(contactPersonMapper::toDto);
    }

    /**
     * Delete the contactPerson by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactPerson : {}", id);
        contactPersonRepository.deleteById(id);
    }
}
