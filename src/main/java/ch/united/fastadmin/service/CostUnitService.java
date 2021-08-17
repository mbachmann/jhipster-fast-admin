package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.CostUnit;
import ch.united.fastadmin.repository.CostUnitRepository;
import ch.united.fastadmin.service.dto.CostUnitDTO;
import ch.united.fastadmin.service.mapper.CostUnitMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CostUnit}.
 */
@Service
@Transactional
public class CostUnitService {

    private final Logger log = LoggerFactory.getLogger(CostUnitService.class);

    private final CostUnitRepository costUnitRepository;

    private final CostUnitMapper costUnitMapper;

    public CostUnitService(CostUnitRepository costUnitRepository, CostUnitMapper costUnitMapper) {
        this.costUnitRepository = costUnitRepository;
        this.costUnitMapper = costUnitMapper;
    }

    /**
     * Save a costUnit.
     *
     * @param costUnitDTO the entity to save.
     * @return the persisted entity.
     */
    public CostUnitDTO save(CostUnitDTO costUnitDTO) {
        log.debug("Request to save CostUnit : {}", costUnitDTO);
        CostUnit costUnit = costUnitMapper.toEntity(costUnitDTO);
        costUnit = costUnitRepository.save(costUnit);
        return costUnitMapper.toDto(costUnit);
    }

    /**
     * Partially update a costUnit.
     *
     * @param costUnitDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CostUnitDTO> partialUpdate(CostUnitDTO costUnitDTO) {
        log.debug("Request to partially update CostUnit : {}", costUnitDTO);

        return costUnitRepository
            .findById(costUnitDTO.getId())
            .map(
                existingCostUnit -> {
                    costUnitMapper.partialUpdate(existingCostUnit, costUnitDTO);

                    return existingCostUnit;
                }
            )
            .map(costUnitRepository::save)
            .map(costUnitMapper::toDto);
    }

    /**
     * Get all the costUnits.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CostUnitDTO> findAll() {
        log.debug("Request to get all CostUnits");
        return costUnitRepository.findAll().stream().map(costUnitMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one costUnit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CostUnitDTO> findOne(Long id) {
        log.debug("Request to get CostUnit : {}", id);
        return costUnitRepository.findById(id).map(costUnitMapper::toDto);
    }

    /**
     * Delete the costUnit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CostUnit : {}", id);
        costUnitRepository.deleteById(id);
    }
}
