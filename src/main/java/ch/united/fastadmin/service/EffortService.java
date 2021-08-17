package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.Effort;
import ch.united.fastadmin.repository.EffortRepository;
import ch.united.fastadmin.service.dto.EffortDTO;
import ch.united.fastadmin.service.mapper.EffortMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Effort}.
 */
@Service
@Transactional
public class EffortService {

    private final Logger log = LoggerFactory.getLogger(EffortService.class);

    private final EffortRepository effortRepository;

    private final EffortMapper effortMapper;

    public EffortService(EffortRepository effortRepository, EffortMapper effortMapper) {
        this.effortRepository = effortRepository;
        this.effortMapper = effortMapper;
    }

    /**
     * Save a effort.
     *
     * @param effortDTO the entity to save.
     * @return the persisted entity.
     */
    public EffortDTO save(EffortDTO effortDTO) {
        log.debug("Request to save Effort : {}", effortDTO);
        Effort effort = effortMapper.toEntity(effortDTO);
        effort = effortRepository.save(effort);
        return effortMapper.toDto(effort);
    }

    /**
     * Partially update a effort.
     *
     * @param effortDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EffortDTO> partialUpdate(EffortDTO effortDTO) {
        log.debug("Request to partially update Effort : {}", effortDTO);

        return effortRepository
            .findById(effortDTO.getId())
            .map(
                existingEffort -> {
                    effortMapper.partialUpdate(existingEffort, effortDTO);

                    return existingEffort;
                }
            )
            .map(effortRepository::save)
            .map(effortMapper::toDto);
    }

    /**
     * Get all the efforts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EffortDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Efforts");
        return effortRepository.findAll(pageable).map(effortMapper::toDto);
    }

    /**
     * Get one effort by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EffortDTO> findOne(Long id) {
        log.debug("Request to get Effort : {}", id);
        return effortRepository.findById(id).map(effortMapper::toDto);
    }

    /**
     * Delete the effort by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Effort : {}", id);
        effortRepository.deleteById(id);
    }
}
