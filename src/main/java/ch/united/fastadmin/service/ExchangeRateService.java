package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.ExchangeRate;
import ch.united.fastadmin.repository.ExchangeRateRepository;
import ch.united.fastadmin.service.dto.ExchangeRateDTO;
import ch.united.fastadmin.service.mapper.ExchangeRateMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExchangeRate}.
 */
@Service
@Transactional
public class ExchangeRateService {

    private final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);

    private final ExchangeRateRepository exchangeRateRepository;

    private final ExchangeRateMapper exchangeRateMapper;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, ExchangeRateMapper exchangeRateMapper) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    /**
     * Save a exchangeRate.
     *
     * @param exchangeRateDTO the entity to save.
     * @return the persisted entity.
     */
    public ExchangeRateDTO save(ExchangeRateDTO exchangeRateDTO) {
        log.debug("Request to save ExchangeRate : {}", exchangeRateDTO);
        ExchangeRate exchangeRate = exchangeRateMapper.toEntity(exchangeRateDTO);
        exchangeRate = exchangeRateRepository.save(exchangeRate);
        return exchangeRateMapper.toDto(exchangeRate);
    }

    /**
     * Partially update a exchangeRate.
     *
     * @param exchangeRateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExchangeRateDTO> partialUpdate(ExchangeRateDTO exchangeRateDTO) {
        log.debug("Request to partially update ExchangeRate : {}", exchangeRateDTO);

        return exchangeRateRepository
            .findById(exchangeRateDTO.getId())
            .map(
                existingExchangeRate -> {
                    exchangeRateMapper.partialUpdate(existingExchangeRate, exchangeRateDTO);

                    return existingExchangeRate;
                }
            )
            .map(exchangeRateRepository::save)
            .map(exchangeRateMapper::toDto);
    }

    /**
     * Get all the exchangeRates.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExchangeRateDTO> findAll() {
        log.debug("Request to get all ExchangeRates");
        return exchangeRateRepository.findAll().stream().map(exchangeRateMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one exchangeRate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExchangeRateDTO> findOne(Long id) {
        log.debug("Request to get ExchangeRate : {}", id);
        return exchangeRateRepository.findById(id).map(exchangeRateMapper::toDto);
    }

    /**
     * Delete the exchangeRate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExchangeRate : {}", id);
        exchangeRateRepository.deleteById(id);
    }
}
