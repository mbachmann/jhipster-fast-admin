package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.Signature;
import ch.united.fastadmin.repository.SignatureRepository;
import ch.united.fastadmin.service.dto.SignatureDTO;
import ch.united.fastadmin.service.mapper.SignatureMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Signature}.
 */
@Service
@Transactional
public class SignatureService {

    private final Logger log = LoggerFactory.getLogger(SignatureService.class);

    private final SignatureRepository signatureRepository;

    private final SignatureMapper signatureMapper;

    public SignatureService(SignatureRepository signatureRepository, SignatureMapper signatureMapper) {
        this.signatureRepository = signatureRepository;
        this.signatureMapper = signatureMapper;
    }

    /**
     * Save a signature.
     *
     * @param signatureDTO the entity to save.
     * @return the persisted entity.
     */
    public SignatureDTO save(SignatureDTO signatureDTO) {
        log.debug("Request to save Signature : {}", signatureDTO);
        Signature signature = signatureMapper.toEntity(signatureDTO);
        signature = signatureRepository.save(signature);
        return signatureMapper.toDto(signature);
    }

    /**
     * Partially update a signature.
     *
     * @param signatureDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SignatureDTO> partialUpdate(SignatureDTO signatureDTO) {
        log.debug("Request to partially update Signature : {}", signatureDTO);

        return signatureRepository
            .findById(signatureDTO.getId())
            .map(
                existingSignature -> {
                    signatureMapper.partialUpdate(existingSignature, signatureDTO);

                    return existingSignature;
                }
            )
            .map(signatureRepository::save)
            .map(signatureMapper::toDto);
    }

    /**
     * Get all the signatures.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SignatureDTO> findAll() {
        log.debug("Request to get all Signatures");
        return signatureRepository.findAll().stream().map(signatureMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one signature by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SignatureDTO> findOne(Long id) {
        log.debug("Request to get Signature : {}", id);
        return signatureRepository.findById(id).map(signatureMapper::toDto);
    }

    /**
     * Delete the signature by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Signature : {}", id);
        signatureRepository.deleteById(id);
    }
}
