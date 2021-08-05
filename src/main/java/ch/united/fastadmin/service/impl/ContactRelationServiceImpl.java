package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.ContactRelation;
import ch.united.fastadmin.repository.ContactRelationRepository;
import ch.united.fastadmin.repository.search.ContactRelationSearchRepository;
import ch.united.fastadmin.service.ContactRelationService;
import ch.united.fastadmin.service.dto.ContactRelationDTO;
import ch.united.fastadmin.service.mapper.ContactRelationMapper;
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
 * Service Implementation for managing {@link ContactRelation}.
 */
@Service
@Transactional
public class ContactRelationServiceImpl implements ContactRelationService {

    private final Logger log = LoggerFactory.getLogger(ContactRelationServiceImpl.class);

    private final ContactRelationRepository contactRelationRepository;

    private final ContactRelationMapper contactRelationMapper;

    private final ContactRelationSearchRepository contactRelationSearchRepository;

    public ContactRelationServiceImpl(
        ContactRelationRepository contactRelationRepository,
        ContactRelationMapper contactRelationMapper,
        ContactRelationSearchRepository contactRelationSearchRepository
    ) {
        this.contactRelationRepository = contactRelationRepository;
        this.contactRelationMapper = contactRelationMapper;
        this.contactRelationSearchRepository = contactRelationSearchRepository;
    }

    @Override
    public ContactRelationDTO save(ContactRelationDTO contactRelationDTO) {
        log.debug("Request to save ContactRelation : {}", contactRelationDTO);
        ContactRelation contactRelation = contactRelationMapper.toEntity(contactRelationDTO);
        contactRelation = contactRelationRepository.save(contactRelation);
        ContactRelationDTO result = contactRelationMapper.toDto(contactRelation);
        contactRelationSearchRepository.save(contactRelation);
        return result;
    }

    @Override
    public Optional<ContactRelationDTO> partialUpdate(ContactRelationDTO contactRelationDTO) {
        log.debug("Request to partially update ContactRelation : {}", contactRelationDTO);

        return contactRelationRepository
            .findById(contactRelationDTO.getId())
            .map(
                existingContactRelation -> {
                    contactRelationMapper.partialUpdate(existingContactRelation, contactRelationDTO);

                    return existingContactRelation;
                }
            )
            .map(contactRelationRepository::save)
            .map(
                savedContactRelation -> {
                    contactRelationSearchRepository.save(savedContactRelation);

                    return savedContactRelation;
                }
            )
            .map(contactRelationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactRelationDTO> findAll() {
        log.debug("Request to get all ContactRelations");
        return contactRelationRepository
            .findAll()
            .stream()
            .map(contactRelationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactRelationDTO> findOne(Long id) {
        log.debug("Request to get ContactRelation : {}", id);
        return contactRelationRepository.findById(id).map(contactRelationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactRelation : {}", id);
        contactRelationRepository.deleteById(id);
        contactRelationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactRelationDTO> search(String query) {
        log.debug("Request to search ContactRelations for query {}", query);
        return StreamSupport
            .stream(contactRelationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(contactRelationMapper::toDto)
            .collect(Collectors.toList());
    }
}
