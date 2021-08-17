package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.DocumentFreeText;
import ch.united.fastadmin.repository.DocumentFreeTextRepository;
import ch.united.fastadmin.service.dto.DocumentFreeTextDTO;
import ch.united.fastadmin.service.mapper.DocumentFreeTextMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocumentFreeText}.
 */
@Service
@Transactional
public class DocumentFreeTextService {

    private final Logger log = LoggerFactory.getLogger(DocumentFreeTextService.class);

    private final DocumentFreeTextRepository documentFreeTextRepository;

    private final DocumentFreeTextMapper documentFreeTextMapper;

    public DocumentFreeTextService(DocumentFreeTextRepository documentFreeTextRepository, DocumentFreeTextMapper documentFreeTextMapper) {
        this.documentFreeTextRepository = documentFreeTextRepository;
        this.documentFreeTextMapper = documentFreeTextMapper;
    }

    /**
     * Save a documentFreeText.
     *
     * @param documentFreeTextDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentFreeTextDTO save(DocumentFreeTextDTO documentFreeTextDTO) {
        log.debug("Request to save DocumentFreeText : {}", documentFreeTextDTO);
        DocumentFreeText documentFreeText = documentFreeTextMapper.toEntity(documentFreeTextDTO);
        documentFreeText = documentFreeTextRepository.save(documentFreeText);
        return documentFreeTextMapper.toDto(documentFreeText);
    }

    /**
     * Partially update a documentFreeText.
     *
     * @param documentFreeTextDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentFreeTextDTO> partialUpdate(DocumentFreeTextDTO documentFreeTextDTO) {
        log.debug("Request to partially update DocumentFreeText : {}", documentFreeTextDTO);

        return documentFreeTextRepository
            .findById(documentFreeTextDTO.getId())
            .map(
                existingDocumentFreeText -> {
                    documentFreeTextMapper.partialUpdate(existingDocumentFreeText, documentFreeTextDTO);

                    return existingDocumentFreeText;
                }
            )
            .map(documentFreeTextRepository::save)
            .map(documentFreeTextMapper::toDto);
    }

    /**
     * Get all the documentFreeTexts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentFreeTextDTO> findAll() {
        log.debug("Request to get all DocumentFreeTexts");
        return documentFreeTextRepository
            .findAll()
            .stream()
            .map(documentFreeTextMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one documentFreeText by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentFreeTextDTO> findOne(Long id) {
        log.debug("Request to get DocumentFreeText : {}", id);
        return documentFreeTextRepository.findById(id).map(documentFreeTextMapper::toDto);
    }

    /**
     * Delete the documentFreeText by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocumentFreeText : {}", id);
        documentFreeTextRepository.deleteById(id);
    }
}
