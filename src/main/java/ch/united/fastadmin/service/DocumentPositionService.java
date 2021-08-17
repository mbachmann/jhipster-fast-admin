package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.DocumentPosition;
import ch.united.fastadmin.repository.DocumentPositionRepository;
import ch.united.fastadmin.service.dto.DocumentPositionDTO;
import ch.united.fastadmin.service.mapper.DocumentPositionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocumentPosition}.
 */
@Service
@Transactional
public class DocumentPositionService {

    private final Logger log = LoggerFactory.getLogger(DocumentPositionService.class);

    private final DocumentPositionRepository documentPositionRepository;

    private final DocumentPositionMapper documentPositionMapper;

    public DocumentPositionService(DocumentPositionRepository documentPositionRepository, DocumentPositionMapper documentPositionMapper) {
        this.documentPositionRepository = documentPositionRepository;
        this.documentPositionMapper = documentPositionMapper;
    }

    /**
     * Save a documentPosition.
     *
     * @param documentPositionDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentPositionDTO save(DocumentPositionDTO documentPositionDTO) {
        log.debug("Request to save DocumentPosition : {}", documentPositionDTO);
        DocumentPosition documentPosition = documentPositionMapper.toEntity(documentPositionDTO);
        documentPosition = documentPositionRepository.save(documentPosition);
        return documentPositionMapper.toDto(documentPosition);
    }

    /**
     * Partially update a documentPosition.
     *
     * @param documentPositionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentPositionDTO> partialUpdate(DocumentPositionDTO documentPositionDTO) {
        log.debug("Request to partially update DocumentPosition : {}", documentPositionDTO);

        return documentPositionRepository
            .findById(documentPositionDTO.getId())
            .map(
                existingDocumentPosition -> {
                    documentPositionMapper.partialUpdate(existingDocumentPosition, documentPositionDTO);

                    return existingDocumentPosition;
                }
            )
            .map(documentPositionRepository::save)
            .map(documentPositionMapper::toDto);
    }

    /**
     * Get all the documentPositions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentPositionDTO> findAll() {
        log.debug("Request to get all DocumentPositions");
        return documentPositionRepository
            .findAll()
            .stream()
            .map(documentPositionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one documentPosition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentPositionDTO> findOne(Long id) {
        log.debug("Request to get DocumentPosition : {}", id);
        return documentPositionRepository.findById(id).map(documentPositionMapper::toDto);
    }

    /**
     * Delete the documentPosition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocumentPosition : {}", id);
        documentPositionRepository.deleteById(id);
    }
}
