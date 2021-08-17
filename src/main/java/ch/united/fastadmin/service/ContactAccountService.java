package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.ContactAccount;
import ch.united.fastadmin.repository.ContactAccountRepository;
import ch.united.fastadmin.service.dto.ContactAccountDTO;
import ch.united.fastadmin.service.mapper.ContactAccountMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContactAccount}.
 */
@Service
@Transactional
public class ContactAccountService {

    private final Logger log = LoggerFactory.getLogger(ContactAccountService.class);

    private final ContactAccountRepository contactAccountRepository;

    private final ContactAccountMapper contactAccountMapper;

    public ContactAccountService(ContactAccountRepository contactAccountRepository, ContactAccountMapper contactAccountMapper) {
        this.contactAccountRepository = contactAccountRepository;
        this.contactAccountMapper = contactAccountMapper;
    }

    /**
     * Save a contactAccount.
     *
     * @param contactAccountDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactAccountDTO save(ContactAccountDTO contactAccountDTO) {
        log.debug("Request to save ContactAccount : {}", contactAccountDTO);
        ContactAccount contactAccount = contactAccountMapper.toEntity(contactAccountDTO);
        contactAccount = contactAccountRepository.save(contactAccount);
        return contactAccountMapper.toDto(contactAccount);
    }

    /**
     * Partially update a contactAccount.
     *
     * @param contactAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactAccountDTO> partialUpdate(ContactAccountDTO contactAccountDTO) {
        log.debug("Request to partially update ContactAccount : {}", contactAccountDTO);

        return contactAccountRepository
            .findById(contactAccountDTO.getId())
            .map(
                existingContactAccount -> {
                    contactAccountMapper.partialUpdate(existingContactAccount, contactAccountDTO);

                    return existingContactAccount;
                }
            )
            .map(contactAccountRepository::save)
            .map(contactAccountMapper::toDto);
    }

    /**
     * Get all the contactAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactAccounts");
        return contactAccountRepository.findAll(pageable).map(contactAccountMapper::toDto);
    }

    /**
     * Get one contactAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactAccountDTO> findOne(Long id) {
        log.debug("Request to get ContactAccount : {}", id);
        return contactAccountRepository.findById(id).map(contactAccountMapper::toDto);
    }

    /**
     * Delete the contactAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactAccount : {}", id);
        contactAccountRepository.deleteById(id);
    }
}
