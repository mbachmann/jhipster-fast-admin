package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.CustomField;
import ch.united.fastadmin.repository.CustomFieldRepository;
import ch.united.fastadmin.repository.search.CustomFieldSearchRepository;
import ch.united.fastadmin.service.CustomFieldService;
import ch.united.fastadmin.service.dto.CustomFieldDTO;
import ch.united.fastadmin.service.mapper.CustomFieldMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomField}.
 */
@Service
@Transactional
public class CustomFieldServiceImpl implements CustomFieldService {

    private final Logger log = LoggerFactory.getLogger(CustomFieldServiceImpl.class);

    private final CustomFieldRepository customFieldRepository;

    private final CustomFieldMapper customFieldMapper;

    private final CustomFieldSearchRepository customFieldSearchRepository;

    public CustomFieldServiceImpl(
        CustomFieldRepository customFieldRepository,
        CustomFieldMapper customFieldMapper,
        CustomFieldSearchRepository customFieldSearchRepository
    ) {
        this.customFieldRepository = customFieldRepository;
        this.customFieldMapper = customFieldMapper;
        this.customFieldSearchRepository = customFieldSearchRepository;
    }

    @Override
    public CustomFieldDTO save(CustomFieldDTO customFieldDTO) {
        log.debug("Request to save CustomField : {}", customFieldDTO);
        CustomField customField = customFieldMapper.toEntity(customFieldDTO);
        customField = customFieldRepository.save(customField);
        CustomFieldDTO result = customFieldMapper.toDto(customField);
        customFieldSearchRepository.save(customField);
        return result;
    }

    @Override
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
            .map(
                savedCustomField -> {
                    customFieldSearchRepository.save(savedCustomField);

                    return savedCustomField;
                }
            )
            .map(customFieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomFieldDTO> findAll() {
        log.debug("Request to get all CustomFields");
        return customFieldRepository.findAll().stream().map(customFieldMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomFieldDTO> findOne(Long id) {
        log.debug("Request to get CustomField : {}", id);
        return customFieldRepository.findById(id).map(customFieldMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomField : {}", id);
        customFieldRepository.deleteById(id);
        customFieldSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomFieldDTO> search(String query) {
        log.debug("Request to search CustomFields for query {}", query);
        return StreamSupport
            .stream(customFieldSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(customFieldMapper::toDto)
            .collect(Collectors.toList());
    }
}
