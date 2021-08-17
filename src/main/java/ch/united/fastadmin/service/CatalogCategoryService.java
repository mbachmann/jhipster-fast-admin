package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.CatalogCategory;
import ch.united.fastadmin.repository.CatalogCategoryRepository;
import ch.united.fastadmin.service.dto.CatalogCategoryDTO;
import ch.united.fastadmin.service.mapper.CatalogCategoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CatalogCategory}.
 */
@Service
@Transactional
public class CatalogCategoryService {

    private final Logger log = LoggerFactory.getLogger(CatalogCategoryService.class);

    private final CatalogCategoryRepository catalogCategoryRepository;

    private final CatalogCategoryMapper catalogCategoryMapper;

    public CatalogCategoryService(CatalogCategoryRepository catalogCategoryRepository, CatalogCategoryMapper catalogCategoryMapper) {
        this.catalogCategoryRepository = catalogCategoryRepository;
        this.catalogCategoryMapper = catalogCategoryMapper;
    }

    /**
     * Save a catalogCategory.
     *
     * @param catalogCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogCategoryDTO save(CatalogCategoryDTO catalogCategoryDTO) {
        log.debug("Request to save CatalogCategory : {}", catalogCategoryDTO);
        CatalogCategory catalogCategory = catalogCategoryMapper.toEntity(catalogCategoryDTO);
        catalogCategory = catalogCategoryRepository.save(catalogCategory);
        return catalogCategoryMapper.toDto(catalogCategory);
    }

    /**
     * Partially update a catalogCategory.
     *
     * @param catalogCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogCategoryDTO> partialUpdate(CatalogCategoryDTO catalogCategoryDTO) {
        log.debug("Request to partially update CatalogCategory : {}", catalogCategoryDTO);

        return catalogCategoryRepository
            .findById(catalogCategoryDTO.getId())
            .map(
                existingCatalogCategory -> {
                    catalogCategoryMapper.partialUpdate(existingCatalogCategory, catalogCategoryDTO);

                    return existingCatalogCategory;
                }
            )
            .map(catalogCategoryRepository::save)
            .map(catalogCategoryMapper::toDto);
    }

    /**
     * Get all the catalogCategories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CatalogCategoryDTO> findAll() {
        log.debug("Request to get all CatalogCategories");
        return catalogCategoryRepository
            .findAll()
            .stream()
            .map(catalogCategoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one catalogCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogCategoryDTO> findOne(Long id) {
        log.debug("Request to get CatalogCategory : {}", id);
        return catalogCategoryRepository.findById(id).map(catalogCategoryMapper::toDto);
    }

    /**
     * Delete the catalogCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CatalogCategory : {}", id);
        catalogCategoryRepository.deleteById(id);
    }
}
