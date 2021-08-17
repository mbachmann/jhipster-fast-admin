package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.CustomFieldValue;
import ch.united.fastadmin.repository.CustomFieldValueRepository;
import ch.united.fastadmin.service.dto.CustomFieldValueDTO;
import ch.united.fastadmin.service.mapper.CustomFieldValueMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomFieldValue}.
 */
@Service
@Transactional
public class CustomFieldValueService {

    private final Logger log = LoggerFactory.getLogger(CustomFieldValueService.class);

    private final CustomFieldValueRepository customFieldValueRepository;

    private final CustomFieldValueMapper customFieldValueMapper;

    public CustomFieldValueService(CustomFieldValueRepository customFieldValueRepository, CustomFieldValueMapper customFieldValueMapper) {
        this.customFieldValueRepository = customFieldValueRepository;
        this.customFieldValueMapper = customFieldValueMapper;
    }

    /**
     * Save a customFieldValue.
     *
     * @param customFieldValueDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomFieldValueDTO save(CustomFieldValueDTO customFieldValueDTO) {
        log.debug("Request to save CustomFieldValue : {}", customFieldValueDTO);
        CustomFieldValue customFieldValue = customFieldValueMapper.toEntity(customFieldValueDTO);
        customFieldValue = customFieldValueRepository.save(customFieldValue);
        return customFieldValueMapper.toDto(customFieldValue);
    }

    /**
     * Partially update a customFieldValue.
     *
     * @param customFieldValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomFieldValueDTO> partialUpdate(CustomFieldValueDTO customFieldValueDTO) {
        log.debug("Request to partially update CustomFieldValue : {}", customFieldValueDTO);

        return customFieldValueRepository
            .findById(customFieldValueDTO.getId())
            .map(
                existingCustomFieldValue -> {
                    customFieldValueMapper.partialUpdate(existingCustomFieldValue, customFieldValueDTO);

                    return existingCustomFieldValue;
                }
            )
            .map(customFieldValueRepository::save)
            .map(customFieldValueMapper::toDto);
    }

    /**
     * Get all the customFieldValues.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CustomFieldValueDTO> findAll() {
        log.debug("Request to get all CustomFieldValues");
        return customFieldValueRepository
            .findAll()
            .stream()
            .map(customFieldValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one customFieldValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomFieldValueDTO> findOne(Long id) {
        log.debug("Request to get CustomFieldValue : {}", id);
        return customFieldValueRepository.findById(id).map(customFieldValueMapper::toDto);
    }

    /**
     * Delete the customFieldValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomFieldValue : {}", id);
        customFieldValueRepository.deleteById(id);
    }
}
