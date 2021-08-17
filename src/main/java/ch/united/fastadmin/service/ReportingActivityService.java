package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.ReportingActivity;
import ch.united.fastadmin.repository.ReportingActivityRepository;
import ch.united.fastadmin.service.dto.ReportingActivityDTO;
import ch.united.fastadmin.service.mapper.ReportingActivityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReportingActivity}.
 */
@Service
@Transactional
public class ReportingActivityService {

    private final Logger log = LoggerFactory.getLogger(ReportingActivityService.class);

    private final ReportingActivityRepository reportingActivityRepository;

    private final ReportingActivityMapper reportingActivityMapper;

    public ReportingActivityService(
        ReportingActivityRepository reportingActivityRepository,
        ReportingActivityMapper reportingActivityMapper
    ) {
        this.reportingActivityRepository = reportingActivityRepository;
        this.reportingActivityMapper = reportingActivityMapper;
    }

    /**
     * Save a reportingActivity.
     *
     * @param reportingActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportingActivityDTO save(ReportingActivityDTO reportingActivityDTO) {
        log.debug("Request to save ReportingActivity : {}", reportingActivityDTO);
        ReportingActivity reportingActivity = reportingActivityMapper.toEntity(reportingActivityDTO);
        reportingActivity = reportingActivityRepository.save(reportingActivity);
        return reportingActivityMapper.toDto(reportingActivity);
    }

    /**
     * Partially update a reportingActivity.
     *
     * @param reportingActivityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportingActivityDTO> partialUpdate(ReportingActivityDTO reportingActivityDTO) {
        log.debug("Request to partially update ReportingActivity : {}", reportingActivityDTO);

        return reportingActivityRepository
            .findById(reportingActivityDTO.getId())
            .map(
                existingReportingActivity -> {
                    reportingActivityMapper.partialUpdate(existingReportingActivity, reportingActivityDTO);

                    return existingReportingActivity;
                }
            )
            .map(reportingActivityRepository::save)
            .map(reportingActivityMapper::toDto);
    }

    /**
     * Get all the reportingActivities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportingActivityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportingActivities");
        return reportingActivityRepository.findAll(pageable).map(reportingActivityMapper::toDto);
    }

    /**
     * Get one reportingActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportingActivityDTO> findOne(Long id) {
        log.debug("Request to get ReportingActivity : {}", id);
        return reportingActivityRepository.findById(id).map(reportingActivityMapper::toDto);
    }

    /**
     * Delete the reportingActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReportingActivity : {}", id);
        reportingActivityRepository.deleteById(id);
    }
}
