package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.Isr;
import ch.united.fastadmin.repository.IsrRepository;
import ch.united.fastadmin.service.dto.IsrDTO;
import ch.united.fastadmin.service.mapper.IsrMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Isr}.
 */
@Service
@Transactional
public class IsrService {

    private final Logger log = LoggerFactory.getLogger(IsrService.class);

    private final IsrRepository isrRepository;

    private final IsrMapper isrMapper;

    public IsrService(IsrRepository isrRepository, IsrMapper isrMapper) {
        this.isrRepository = isrRepository;
        this.isrMapper = isrMapper;
    }

    /**
     * Save a isr.
     *
     * @param isrDTO the entity to save.
     * @return the persisted entity.
     */
    public IsrDTO save(IsrDTO isrDTO) {
        log.debug("Request to save Isr : {}", isrDTO);
        Isr isr = isrMapper.toEntity(isrDTO);
        isr = isrRepository.save(isr);
        return isrMapper.toDto(isr);
    }

    /**
     * Partially update a isr.
     *
     * @param isrDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IsrDTO> partialUpdate(IsrDTO isrDTO) {
        log.debug("Request to partially update Isr : {}", isrDTO);

        return isrRepository
            .findById(isrDTO.getId())
            .map(
                existingIsr -> {
                    isrMapper.partialUpdate(existingIsr, isrDTO);

                    return existingIsr;
                }
            )
            .map(isrRepository::save)
            .map(isrMapper::toDto);
    }

    /**
     * Get all the isrs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IsrDTO> findAll() {
        log.debug("Request to get all Isrs");
        return isrRepository.findAll().stream().map(isrMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one isr by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IsrDTO> findOne(Long id) {
        log.debug("Request to get Isr : {}", id);
        return isrRepository.findById(id).map(isrMapper::toDto);
    }

    /**
     * Delete the isr by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Isr : {}", id);
        isrRepository.deleteById(id);
    }
}
