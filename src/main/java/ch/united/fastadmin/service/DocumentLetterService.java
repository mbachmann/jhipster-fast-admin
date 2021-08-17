package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.DocumentLetter;
import ch.united.fastadmin.repository.DocumentLetterRepository;
import ch.united.fastadmin.service.dto.DocumentLetterDTO;
import ch.united.fastadmin.service.mapper.DocumentLetterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocumentLetter}.
 */
@Service
@Transactional
public class DocumentLetterService {

    private final Logger log = LoggerFactory.getLogger(DocumentLetterService.class);

    private final DocumentLetterRepository documentLetterRepository;

    private final DocumentLetterMapper documentLetterMapper;

    public DocumentLetterService(DocumentLetterRepository documentLetterRepository, DocumentLetterMapper documentLetterMapper) {
        this.documentLetterRepository = documentLetterRepository;
        this.documentLetterMapper = documentLetterMapper;
    }

    /**
     * Save a documentLetter.
     *
     * @param documentLetterDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentLetterDTO save(DocumentLetterDTO documentLetterDTO) {
        log.debug("Request to save DocumentLetter : {}", documentLetterDTO);
        DocumentLetter documentLetter = documentLetterMapper.toEntity(documentLetterDTO);
        documentLetter = documentLetterRepository.save(documentLetter);
        return documentLetterMapper.toDto(documentLetter);
    }

    /**
     * Partially update a documentLetter.
     *
     * @param documentLetterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentLetterDTO> partialUpdate(DocumentLetterDTO documentLetterDTO) {
        log.debug("Request to partially update DocumentLetter : {}", documentLetterDTO);

        return documentLetterRepository
            .findById(documentLetterDTO.getId())
            .map(
                existingDocumentLetter -> {
                    documentLetterMapper.partialUpdate(existingDocumentLetter, documentLetterDTO);

                    return existingDocumentLetter;
                }
            )
            .map(documentLetterRepository::save)
            .map(documentLetterMapper::toDto);
    }

    /**
     * Get all the documentLetters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentLetterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentLetters");
        return documentLetterRepository.findAll(pageable).map(documentLetterMapper::toDto);
    }

    /**
     * Get one documentLetter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentLetterDTO> findOne(Long id) {
        log.debug("Request to get DocumentLetter : {}", id);
        return documentLetterRepository.findById(id).map(documentLetterMapper::toDto);
    }

    /**
     * Delete the documentLetter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocumentLetter : {}", id);
        documentLetterRepository.deleteById(id);
    }
}
