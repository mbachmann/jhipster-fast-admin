package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.Owner;
import ch.united.fastadmin.repository.OwnerRepository;
import ch.united.fastadmin.repository.search.OwnerSearchRepository;
import ch.united.fastadmin.service.OwnerService;
import ch.united.fastadmin.service.dto.OwnerDTO;
import ch.united.fastadmin.service.mapper.OwnerMapper;
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
 * Service Implementation for managing {@link Owner}.
 */
@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private final Logger log = LoggerFactory.getLogger(OwnerServiceImpl.class);

    private final OwnerRepository ownerRepository;

    private final OwnerMapper ownerMapper;

    private final OwnerSearchRepository ownerSearchRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository, OwnerMapper ownerMapper, OwnerSearchRepository ownerSearchRepository) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
        this.ownerSearchRepository = ownerSearchRepository;
    }

    @Override
    public OwnerDTO save(OwnerDTO ownerDTO) {
        log.debug("Request to save Owner : {}", ownerDTO);
        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner = ownerRepository.save(owner);
        OwnerDTO result = ownerMapper.toDto(owner);
        ownerSearchRepository.save(owner);
        return result;
    }

    @Override
    public Optional<OwnerDTO> partialUpdate(OwnerDTO ownerDTO) {
        log.debug("Request to partially update Owner : {}", ownerDTO);

        return ownerRepository
            .findById(ownerDTO.getId())
            .map(
                existingOwner -> {
                    ownerMapper.partialUpdate(existingOwner, ownerDTO);

                    return existingOwner;
                }
            )
            .map(ownerRepository::save)
            .map(
                savedOwner -> {
                    ownerSearchRepository.save(savedOwner);

                    return savedOwner;
                }
            )
            .map(ownerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnerDTO> findAll() {
        log.debug("Request to get all Owners");
        return ownerRepository.findAll().stream().map(ownerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OwnerDTO> findOne(Long id) {
        log.debug("Request to get Owner : {}", id);
        return ownerRepository.findById(id).map(ownerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Owner : {}", id);
        ownerRepository.deleteById(id);
        ownerSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnerDTO> search(String query) {
        log.debug("Request to search Owners for query {}", query);
        return StreamSupport
            .stream(ownerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(ownerMapper::toDto)
            .collect(Collectors.toList());
    }
}
