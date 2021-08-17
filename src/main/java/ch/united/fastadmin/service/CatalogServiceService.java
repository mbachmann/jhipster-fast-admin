package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.CatalogService;
import ch.united.fastadmin.repository.CatalogServiceRepository;
import ch.united.fastadmin.service.dto.CatalogServiceDTO;
import ch.united.fastadmin.service.mapper.CatalogServiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CatalogService}.
 */
@Service
@Transactional
public class CatalogServiceService {

    private final Logger log = LoggerFactory.getLogger(CatalogServiceService.class);

    private final CatalogServiceRepository catalogServiceRepository;

    private final CatalogServiceMapper catalogServiceMapper;

    public CatalogServiceService(CatalogServiceRepository catalogServiceRepository, CatalogServiceMapper catalogServiceMapper) {
        this.catalogServiceRepository = catalogServiceRepository;
        this.catalogServiceMapper = catalogServiceMapper;
    }

    /**
     * Save a catalogService.
     *
     * @param catalogServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogServiceDTO save(CatalogServiceDTO catalogServiceDTO) {
        log.debug("Request to save CatalogService : {}", catalogServiceDTO);
        CatalogService catalogService = catalogServiceMapper.toEntity(catalogServiceDTO);
        catalogService = catalogServiceRepository.save(catalogService);
        return catalogServiceMapper.toDto(catalogService);
    }

    /**
     * Partially update a catalogService.
     *
     * @param catalogServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogServiceDTO> partialUpdate(CatalogServiceDTO catalogServiceDTO) {
        log.debug("Request to partially update CatalogService : {}", catalogServiceDTO);

        return catalogServiceRepository
            .findById(catalogServiceDTO.getId())
            .map(
                existingCatalogService -> {
                    catalogServiceMapper.partialUpdate(existingCatalogService, catalogServiceDTO);

                    return existingCatalogService;
                }
            )
            .map(catalogServiceRepository::save)
            .map(catalogServiceMapper::toDto);
    }

    /**
     * Get all the catalogServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogServices");
        return catalogServiceRepository.findAll(pageable).map(catalogServiceMapper::toDto);
    }

    /**
     * Get one catalogService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogServiceDTO> findOne(Long id) {
        log.debug("Request to get CatalogService : {}", id);
        return catalogServiceRepository.findById(id).map(catalogServiceMapper::toDto);
    }

    /**
     * Delete the catalogService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CatalogService : {}", id);
        catalogServiceRepository.deleteById(id);
    }
}
