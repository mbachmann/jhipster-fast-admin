package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.Layout;
import ch.united.fastadmin.repository.LayoutRepository;
import ch.united.fastadmin.service.dto.LayoutDTO;
import ch.united.fastadmin.service.mapper.LayoutMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Layout}.
 */
@Service
@Transactional
public class LayoutService {

    private final Logger log = LoggerFactory.getLogger(LayoutService.class);

    private final LayoutRepository layoutRepository;

    private final LayoutMapper layoutMapper;

    public LayoutService(LayoutRepository layoutRepository, LayoutMapper layoutMapper) {
        this.layoutRepository = layoutRepository;
        this.layoutMapper = layoutMapper;
    }

    /**
     * Save a layout.
     *
     * @param layoutDTO the entity to save.
     * @return the persisted entity.
     */
    public LayoutDTO save(LayoutDTO layoutDTO) {
        log.debug("Request to save Layout : {}", layoutDTO);
        Layout layout = layoutMapper.toEntity(layoutDTO);
        layout = layoutRepository.save(layout);
        return layoutMapper.toDto(layout);
    }

    /**
     * Partially update a layout.
     *
     * @param layoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LayoutDTO> partialUpdate(LayoutDTO layoutDTO) {
        log.debug("Request to partially update Layout : {}", layoutDTO);

        return layoutRepository
            .findById(layoutDTO.getId())
            .map(
                existingLayout -> {
                    layoutMapper.partialUpdate(existingLayout, layoutDTO);

                    return existingLayout;
                }
            )
            .map(layoutRepository::save)
            .map(layoutMapper::toDto);
    }

    /**
     * Get all the layouts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LayoutDTO> findAll() {
        log.debug("Request to get all Layouts");
        return layoutRepository.findAll().stream().map(layoutMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one layout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LayoutDTO> findOne(Long id) {
        log.debug("Request to get Layout : {}", id);
        return layoutRepository.findById(id).map(layoutMapper::toDto);
    }

    /**
     * Delete the layout by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Layout : {}", id);
        layoutRepository.deleteById(id);
    }
}
