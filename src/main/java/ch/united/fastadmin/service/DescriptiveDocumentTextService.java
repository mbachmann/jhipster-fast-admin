package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.DescriptiveDocumentText;
import ch.united.fastadmin.repository.DescriptiveDocumentTextRepository;
import ch.united.fastadmin.service.dto.DescriptiveDocumentTextDTO;
import ch.united.fastadmin.service.mapper.DescriptiveDocumentTextMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DescriptiveDocumentText}.
 */
@Service
@Transactional
public class DescriptiveDocumentTextService {

    private final Logger log = LoggerFactory.getLogger(DescriptiveDocumentTextService.class);

    private final DescriptiveDocumentTextRepository descriptiveDocumentTextRepository;

    private final DescriptiveDocumentTextMapper descriptiveDocumentTextMapper;

    public DescriptiveDocumentTextService(
        DescriptiveDocumentTextRepository descriptiveDocumentTextRepository,
        DescriptiveDocumentTextMapper descriptiveDocumentTextMapper
    ) {
        this.descriptiveDocumentTextRepository = descriptiveDocumentTextRepository;
        this.descriptiveDocumentTextMapper = descriptiveDocumentTextMapper;
    }

    /**
     * Save a descriptiveDocumentText.
     *
     * @param descriptiveDocumentTextDTO the entity to save.
     * @return the persisted entity.
     */
    public DescriptiveDocumentTextDTO save(DescriptiveDocumentTextDTO descriptiveDocumentTextDTO) {
        log.debug("Request to save DescriptiveDocumentText : {}", descriptiveDocumentTextDTO);
        DescriptiveDocumentText descriptiveDocumentText = descriptiveDocumentTextMapper.toEntity(descriptiveDocumentTextDTO);
        descriptiveDocumentText = descriptiveDocumentTextRepository.save(descriptiveDocumentText);
        return descriptiveDocumentTextMapper.toDto(descriptiveDocumentText);
    }

    /**
     * Partially update a descriptiveDocumentText.
     *
     * @param descriptiveDocumentTextDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DescriptiveDocumentTextDTO> partialUpdate(DescriptiveDocumentTextDTO descriptiveDocumentTextDTO) {
        log.debug("Request to partially update DescriptiveDocumentText : {}", descriptiveDocumentTextDTO);

        return descriptiveDocumentTextRepository
            .findById(descriptiveDocumentTextDTO.getId())
            .map(
                existingDescriptiveDocumentText -> {
                    descriptiveDocumentTextMapper.partialUpdate(existingDescriptiveDocumentText, descriptiveDocumentTextDTO);

                    return existingDescriptiveDocumentText;
                }
            )
            .map(descriptiveDocumentTextRepository::save)
            .map(descriptiveDocumentTextMapper::toDto);
    }

    /**
     * Get all the descriptiveDocumentTexts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DescriptiveDocumentTextDTO> findAll() {
        log.debug("Request to get all DescriptiveDocumentTexts");
        return descriptiveDocumentTextRepository
            .findAll()
            .stream()
            .map(descriptiveDocumentTextMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one descriptiveDocumentText by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DescriptiveDocumentTextDTO> findOne(Long id) {
        log.debug("Request to get DescriptiveDocumentText : {}", id);
        return descriptiveDocumentTextRepository.findById(id).map(descriptiveDocumentTextMapper::toDto);
    }

    /**
     * Delete the descriptiveDocumentText by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DescriptiveDocumentText : {}", id);
        descriptiveDocumentTextRepository.deleteById(id);
    }
}
