package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.Offer;
import ch.united.fastadmin.repository.OfferRepository;
import ch.united.fastadmin.service.dto.OfferDTO;
import ch.united.fastadmin.service.mapper.OfferMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Offer}.
 */
@Service
@Transactional
public class OfferService {

    private final Logger log = LoggerFactory.getLogger(OfferService.class);

    private final OfferRepository offerRepository;

    private final OfferMapper offerMapper;

    public OfferService(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    /**
     * Save a offer.
     *
     * @param offerDTO the entity to save.
     * @return the persisted entity.
     */
    public OfferDTO save(OfferDTO offerDTO) {
        log.debug("Request to save Offer : {}", offerDTO);
        Offer offer = offerMapper.toEntity(offerDTO);
        offer = offerRepository.save(offer);
        return offerMapper.toDto(offer);
    }

    /**
     * Partially update a offer.
     *
     * @param offerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OfferDTO> partialUpdate(OfferDTO offerDTO) {
        log.debug("Request to partially update Offer : {}", offerDTO);

        return offerRepository
            .findById(offerDTO.getId())
            .map(
                existingOffer -> {
                    offerMapper.partialUpdate(existingOffer, offerDTO);

                    return existingOffer;
                }
            )
            .map(offerRepository::save)
            .map(offerMapper::toDto);
    }

    /**
     * Get all the offers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OfferDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Offers");
        return offerRepository.findAll(pageable).map(offerMapper::toDto);
    }

    /**
     * Get one offer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OfferDTO> findOne(Long id) {
        log.debug("Request to get Offer : {}", id);
        return offerRepository.findById(id).map(offerMapper::toDto);
    }

    /**
     * Delete the offer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Offer : {}", id);
        offerRepository.deleteById(id);
    }
}
