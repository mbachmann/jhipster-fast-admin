package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.CatalogUnit;
import ch.united.fastadmin.repository.CatalogUnitRepository;
import ch.united.fastadmin.service.dto.CatalogUnitDTO;
import ch.united.fastadmin.service.mapper.CatalogUnitMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CatalogUnit}.
 */
@Service
@Transactional
public class CatalogUnitService {

    private final Logger log = LoggerFactory.getLogger(CatalogUnitService.class);

    private final CatalogUnitRepository catalogUnitRepository;

    private final CatalogUnitMapper catalogUnitMapper;

    public CatalogUnitService(CatalogUnitRepository catalogUnitRepository, CatalogUnitMapper catalogUnitMapper) {
        this.catalogUnitRepository = catalogUnitRepository;
        this.catalogUnitMapper = catalogUnitMapper;
    }

    /**
     * Save a catalogUnit.
     *
     * @param catalogUnitDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogUnitDTO save(CatalogUnitDTO catalogUnitDTO) {
        log.debug("Request to save CatalogUnit : {}", catalogUnitDTO);
        CatalogUnit catalogUnit = catalogUnitMapper.toEntity(catalogUnitDTO);
        catalogUnit = catalogUnitRepository.save(catalogUnit);
        return catalogUnitMapper.toDto(catalogUnit);
    }

    /**
     * Partially update a catalogUnit.
     *
     * @param catalogUnitDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogUnitDTO> partialUpdate(CatalogUnitDTO catalogUnitDTO) {
        log.debug("Request to partially update CatalogUnit : {}", catalogUnitDTO);

        return catalogUnitRepository
            .findById(catalogUnitDTO.getId())
            .map(
                existingCatalogUnit -> {
                    catalogUnitMapper.partialUpdate(existingCatalogUnit, catalogUnitDTO);

                    return existingCatalogUnit;
                }
            )
            .map(catalogUnitRepository::save)
            .map(catalogUnitMapper::toDto);
    }

    /**
     * Get all the catalogUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogUnitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogUnits");
        return catalogUnitRepository.findAll(pageable).map(catalogUnitMapper::toDto);
    }

    /**
     * Get one catalogUnit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogUnitDTO> findOne(Long id) {
        log.debug("Request to get CatalogUnit : {}", id);
        return catalogUnitRepository.findById(id).map(catalogUnitMapper::toDto);
    }

    /**
     * Delete the catalogUnit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CatalogUnit : {}", id);
        catalogUnitRepository.deleteById(id);
    }
}
