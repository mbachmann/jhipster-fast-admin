package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.ValueAddedTax;
import ch.united.fastadmin.repository.ValueAddedTaxRepository;
import ch.united.fastadmin.service.dto.ValueAddedTaxDTO;
import ch.united.fastadmin.service.mapper.ValueAddedTaxMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ValueAddedTax}.
 */
@Service
@Transactional
public class ValueAddedTaxService {

    private final Logger log = LoggerFactory.getLogger(ValueAddedTaxService.class);

    private final ValueAddedTaxRepository valueAddedTaxRepository;

    private final ValueAddedTaxMapper valueAddedTaxMapper;

    public ValueAddedTaxService(ValueAddedTaxRepository valueAddedTaxRepository, ValueAddedTaxMapper valueAddedTaxMapper) {
        this.valueAddedTaxRepository = valueAddedTaxRepository;
        this.valueAddedTaxMapper = valueAddedTaxMapper;
    }

    /**
     * Save a valueAddedTax.
     *
     * @param valueAddedTaxDTO the entity to save.
     * @return the persisted entity.
     */
    public ValueAddedTaxDTO save(ValueAddedTaxDTO valueAddedTaxDTO) {
        log.debug("Request to save ValueAddedTax : {}", valueAddedTaxDTO);
        ValueAddedTax valueAddedTax = valueAddedTaxMapper.toEntity(valueAddedTaxDTO);
        valueAddedTax = valueAddedTaxRepository.save(valueAddedTax);
        return valueAddedTaxMapper.toDto(valueAddedTax);
    }

    /**
     * Partially update a valueAddedTax.
     *
     * @param valueAddedTaxDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ValueAddedTaxDTO> partialUpdate(ValueAddedTaxDTO valueAddedTaxDTO) {
        log.debug("Request to partially update ValueAddedTax : {}", valueAddedTaxDTO);

        return valueAddedTaxRepository
            .findById(valueAddedTaxDTO.getId())
            .map(
                existingValueAddedTax -> {
                    valueAddedTaxMapper.partialUpdate(existingValueAddedTax, valueAddedTaxDTO);

                    return existingValueAddedTax;
                }
            )
            .map(valueAddedTaxRepository::save)
            .map(valueAddedTaxMapper::toDto);
    }

    /**
     * Get all the valueAddedTaxes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ValueAddedTaxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ValueAddedTaxes");
        return valueAddedTaxRepository.findAll(pageable).map(valueAddedTaxMapper::toDto);
    }

    /**
     * Get one valueAddedTax by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ValueAddedTaxDTO> findOne(Long id) {
        log.debug("Request to get ValueAddedTax : {}", id);
        return valueAddedTaxRepository.findById(id).map(valueAddedTaxMapper::toDto);
    }

    /**
     * Delete the valueAddedTax by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ValueAddedTax : {}", id);
        valueAddedTaxRepository.deleteById(id);
    }
}
