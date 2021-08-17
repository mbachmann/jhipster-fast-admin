package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.FreeText;
import ch.united.fastadmin.repository.FreeTextRepository;
import ch.united.fastadmin.service.dto.FreeTextDTO;
import ch.united.fastadmin.service.mapper.FreeTextMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FreeText}.
 */
@Service
@Transactional
public class FreeTextService {

    private final Logger log = LoggerFactory.getLogger(FreeTextService.class);

    private final FreeTextRepository freeTextRepository;

    private final FreeTextMapper freeTextMapper;

    public FreeTextService(FreeTextRepository freeTextRepository, FreeTextMapper freeTextMapper) {
        this.freeTextRepository = freeTextRepository;
        this.freeTextMapper = freeTextMapper;
    }

    /**
     * Save a freeText.
     *
     * @param freeTextDTO the entity to save.
     * @return the persisted entity.
     */
    public FreeTextDTO save(FreeTextDTO freeTextDTO) {
        log.debug("Request to save FreeText : {}", freeTextDTO);
        FreeText freeText = freeTextMapper.toEntity(freeTextDTO);
        freeText = freeTextRepository.save(freeText);
        return freeTextMapper.toDto(freeText);
    }

    /**
     * Partially update a freeText.
     *
     * @param freeTextDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FreeTextDTO> partialUpdate(FreeTextDTO freeTextDTO) {
        log.debug("Request to partially update FreeText : {}", freeTextDTO);

        return freeTextRepository
            .findById(freeTextDTO.getId())
            .map(
                existingFreeText -> {
                    freeTextMapper.partialUpdate(existingFreeText, freeTextDTO);

                    return existingFreeText;
                }
            )
            .map(freeTextRepository::save)
            .map(freeTextMapper::toDto);
    }

    /**
     * Get all the freeTexts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FreeTextDTO> findAll() {
        log.debug("Request to get all FreeTexts");
        return freeTextRepository.findAll().stream().map(freeTextMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one freeText by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FreeTextDTO> findOne(Long id) {
        log.debug("Request to get FreeText : {}", id);
        return freeTextRepository.findById(id).map(freeTextMapper::toDto);
    }

    /**
     * Delete the freeText by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FreeText : {}", id);
        freeTextRepository.deleteById(id);
    }
}
