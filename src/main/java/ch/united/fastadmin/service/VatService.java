package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.Vat;
import ch.united.fastadmin.repository.VatRepository;
import ch.united.fastadmin.service.dto.VatDTO;
import ch.united.fastadmin.service.mapper.VatMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vat}.
 */
@Service
@Transactional
public class VatService {

    private final Logger log = LoggerFactory.getLogger(VatService.class);

    private final VatRepository vatRepository;

    private final VatMapper vatMapper;

    public VatService(VatRepository vatRepository, VatMapper vatMapper) {
        this.vatRepository = vatRepository;
        this.vatMapper = vatMapper;
    }

    /**
     * Save a vat.
     *
     * @param vatDTO the entity to save.
     * @return the persisted entity.
     */
    public VatDTO save(VatDTO vatDTO) {
        log.debug("Request to save Vat : {}", vatDTO);
        Vat vat = vatMapper.toEntity(vatDTO);
        vat = vatRepository.save(vat);
        return vatMapper.toDto(vat);
    }

    /**
     * Partially update a vat.
     *
     * @param vatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VatDTO> partialUpdate(VatDTO vatDTO) {
        log.debug("Request to partially update Vat : {}", vatDTO);

        return vatRepository
            .findById(vatDTO.getId())
            .map(
                existingVat -> {
                    vatMapper.partialUpdate(existingVat, vatDTO);

                    return existingVat;
                }
            )
            .map(vatRepository::save)
            .map(vatMapper::toDto);
    }

    /**
     * Get all the vats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vats");
        return vatRepository.findAll(pageable).map(vatMapper::toDto);
    }

    /**
     * Get one vat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VatDTO> findOne(Long id) {
        log.debug("Request to get Vat : {}", id);
        return vatRepository.findById(id).map(vatMapper::toDto);
    }

    /**
     * Delete the vat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vat : {}", id);
        vatRepository.deleteById(id);
    }
}
