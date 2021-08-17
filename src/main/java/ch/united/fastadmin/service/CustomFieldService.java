package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.CustomField;
import ch.united.fastadmin.repository.CustomFieldRepository;
import ch.united.fastadmin.service.dto.CustomFieldDTO;
import ch.united.fastadmin.service.mapper.CustomFieldMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomField}.
 */
@Service
@Transactional
public class CustomFieldService {

    private final Logger log = LoggerFactory.getLogger(CustomFieldService.class);

    private final CustomFieldRepository customFieldRepository;

    private final CustomFieldMapper customFieldMapper;

    public CustomFieldService(CustomFieldRepository customFieldRepository, CustomFieldMapper customFieldMapper) {
        this.customFieldRepository = customFieldRepository;
        this.customFieldMapper = customFieldMapper;
    }

    /**
     * Save a customField.
     *
     * @param customFieldDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomFieldDTO save(CustomFieldDTO customFieldDTO) {
        log.debug("Request to save CustomField : {}", customFieldDTO);
        CustomField customField = customFieldMapper.toEntity(customFieldDTO);
        customField = customFieldRepository.save(customField);
        return customFieldMapper.toDto(customField);
    }

    /**
     * Partially update a customField.
     *
     * @param customFieldDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomFieldDTO> partialUpdate(CustomFieldDTO customFieldDTO) {
        log.debug("Request to partially update CustomField : {}", customFieldDTO);

        return customFieldRepository
            .findById(customFieldDTO.getId())
            .map(
                existingCustomField -> {
                    customFieldMapper.partialUpdate(existingCustomField, customFieldDTO);

                    return existingCustomField;
                }
            )
            .map(customFieldRepository::save)
            .map(customFieldMapper::toDto);
    }

    /**
     * Get all the customFields.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CustomFieldDTO> findAll() {
        log.debug("Request to get all CustomFields");
        return customFieldRepository.findAll().stream().map(customFieldMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one customField by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomFieldDTO> findOne(Long id) {
        log.debug("Request to get CustomField : {}", id);
        return customFieldRepository.findById(id).map(customFieldMapper::toDto);
    }

    /**
     * Delete the customField by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomField : {}", id);
        customFieldRepository.deleteById(id);
    }
}
