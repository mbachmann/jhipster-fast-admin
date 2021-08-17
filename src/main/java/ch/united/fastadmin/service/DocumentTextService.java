package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.DocumentText;
import ch.united.fastadmin.repository.DocumentTextRepository;
import ch.united.fastadmin.service.dto.DocumentTextDTO;
import ch.united.fastadmin.service.mapper.DocumentTextMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocumentText}.
 */
@Service
@Transactional
public class DocumentTextService {

    private final Logger log = LoggerFactory.getLogger(DocumentTextService.class);

    private final DocumentTextRepository documentTextRepository;

    private final DocumentTextMapper documentTextMapper;

    public DocumentTextService(DocumentTextRepository documentTextRepository, DocumentTextMapper documentTextMapper) {
        this.documentTextRepository = documentTextRepository;
        this.documentTextMapper = documentTextMapper;
    }

    /**
     * Save a documentText.
     *
     * @param documentTextDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentTextDTO save(DocumentTextDTO documentTextDTO) {
        log.debug("Request to save DocumentText : {}", documentTextDTO);
        DocumentText documentText = documentTextMapper.toEntity(documentTextDTO);
        documentText = documentTextRepository.save(documentText);
        return documentTextMapper.toDto(documentText);
    }

    /**
     * Partially update a documentText.
     *
     * @param documentTextDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentTextDTO> partialUpdate(DocumentTextDTO documentTextDTO) {
        log.debug("Request to partially update DocumentText : {}", documentTextDTO);

        return documentTextRepository
            .findById(documentTextDTO.getId())
            .map(
                existingDocumentText -> {
                    documentTextMapper.partialUpdate(existingDocumentText, documentTextDTO);

                    return existingDocumentText;
                }
            )
            .map(documentTextRepository::save)
            .map(documentTextMapper::toDto);
    }

    /**
     * Get all the documentTexts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentTextDTO> findAll() {
        log.debug("Request to get all DocumentTexts");
        return documentTextRepository.findAll().stream().map(documentTextMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one documentText by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentTextDTO> findOne(Long id) {
        log.debug("Request to get DocumentText : {}", id);
        return documentTextRepository.findById(id).map(documentTextMapper::toDto);
    }

    /**
     * Delete the documentText by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocumentText : {}", id);
        documentTextRepository.deleteById(id);
    }
}
