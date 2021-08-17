package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.CatalogProduct;
import ch.united.fastadmin.repository.CatalogProductRepository;
import ch.united.fastadmin.service.dto.CatalogProductDTO;
import ch.united.fastadmin.service.mapper.CatalogProductMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CatalogProduct}.
 */
@Service
@Transactional
public class CatalogProductService {

    private final Logger log = LoggerFactory.getLogger(CatalogProductService.class);

    private final CatalogProductRepository catalogProductRepository;

    private final CatalogProductMapper catalogProductMapper;

    public CatalogProductService(CatalogProductRepository catalogProductRepository, CatalogProductMapper catalogProductMapper) {
        this.catalogProductRepository = catalogProductRepository;
        this.catalogProductMapper = catalogProductMapper;
    }

    /**
     * Save a catalogProduct.
     *
     * @param catalogProductDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogProductDTO save(CatalogProductDTO catalogProductDTO) {
        log.debug("Request to save CatalogProduct : {}", catalogProductDTO);
        CatalogProduct catalogProduct = catalogProductMapper.toEntity(catalogProductDTO);
        catalogProduct = catalogProductRepository.save(catalogProduct);
        return catalogProductMapper.toDto(catalogProduct);
    }

    /**
     * Partially update a catalogProduct.
     *
     * @param catalogProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogProductDTO> partialUpdate(CatalogProductDTO catalogProductDTO) {
        log.debug("Request to partially update CatalogProduct : {}", catalogProductDTO);

        return catalogProductRepository
            .findById(catalogProductDTO.getId())
            .map(
                existingCatalogProduct -> {
                    catalogProductMapper.partialUpdate(existingCatalogProduct, catalogProductDTO);

                    return existingCatalogProduct;
                }
            )
            .map(catalogProductRepository::save)
            .map(catalogProductMapper::toDto);
    }

    /**
     * Get all the catalogProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogProducts");
        return catalogProductRepository.findAll(pageable).map(catalogProductMapper::toDto);
    }

    /**
     * Get one catalogProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogProductDTO> findOne(Long id) {
        log.debug("Request to get CatalogProduct : {}", id);
        return catalogProductRepository.findById(id).map(catalogProductMapper::toDto);
    }

    /**
     * Delete the catalogProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CatalogProduct : {}", id);
        catalogProductRepository.deleteById(id);
    }
}
