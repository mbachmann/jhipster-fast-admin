package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.JobHistory;
import ch.united.fastadmin.repository.JobHistoryRepository;
import ch.united.fastadmin.repository.search.JobHistorySearchRepository;
import ch.united.fastadmin.service.JobHistoryService;
import ch.united.fastadmin.service.dto.JobHistoryDTO;
import ch.united.fastadmin.service.mapper.JobHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link JobHistory}.
 */
@Service
@Transactional
public class JobHistoryServiceImpl implements JobHistoryService {

    private final Logger log = LoggerFactory.getLogger(JobHistoryServiceImpl.class);

    private final JobHistoryRepository jobHistoryRepository;

    private final JobHistoryMapper jobHistoryMapper;

    private final JobHistorySearchRepository jobHistorySearchRepository;

    public JobHistoryServiceImpl(
        JobHistoryRepository jobHistoryRepository,
        JobHistoryMapper jobHistoryMapper,
        JobHistorySearchRepository jobHistorySearchRepository
    ) {
        this.jobHistoryRepository = jobHistoryRepository;
        this.jobHistoryMapper = jobHistoryMapper;
        this.jobHistorySearchRepository = jobHistorySearchRepository;
    }

    @Override
    public JobHistoryDTO save(JobHistoryDTO jobHistoryDTO) {
        log.debug("Request to save JobHistory : {}", jobHistoryDTO);
        JobHistory jobHistory = jobHistoryMapper.toEntity(jobHistoryDTO);
        jobHistory = jobHistoryRepository.save(jobHistory);
        JobHistoryDTO result = jobHistoryMapper.toDto(jobHistory);
        jobHistorySearchRepository.save(jobHistory);
        return result;
    }

    @Override
    public Optional<JobHistoryDTO> partialUpdate(JobHistoryDTO jobHistoryDTO) {
        log.debug("Request to partially update JobHistory : {}", jobHistoryDTO);

        return jobHistoryRepository
            .findById(jobHistoryDTO.getId())
            .map(
                existingJobHistory -> {
                    jobHistoryMapper.partialUpdate(existingJobHistory, jobHistoryDTO);

                    return existingJobHistory;
                }
            )
            .map(jobHistoryRepository::save)
            .map(
                savedJobHistory -> {
                    jobHistorySearchRepository.save(savedJobHistory);

                    return savedJobHistory;
                }
            )
            .map(jobHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobHistories");
        return jobHistoryRepository.findAll(pageable).map(jobHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobHistoryDTO> findOne(Long id) {
        log.debug("Request to get JobHistory : {}", id);
        return jobHistoryRepository.findById(id).map(jobHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobHistory : {}", id);
        jobHistoryRepository.deleteById(id);
        jobHistorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobHistoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of JobHistories for query {}", query);
        return jobHistorySearchRepository.search(queryStringQuery(query), pageable).map(jobHistoryMapper::toDto);
    }
}
