package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.Location;
import ch.united.fastadmin.repository.LocationRepository;
import ch.united.fastadmin.repository.search.LocationSearchRepository;
import ch.united.fastadmin.service.LocationService;
import ch.united.fastadmin.service.dto.LocationDTO;
import ch.united.fastadmin.service.mapper.LocationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Location}.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    private final LocationSearchRepository locationSearchRepository;

    public LocationServiceImpl(
        LocationRepository locationRepository,
        LocationMapper locationMapper,
        LocationSearchRepository locationSearchRepository
    ) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.locationSearchRepository = locationSearchRepository;
    }

    @Override
    public LocationDTO save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        Location location = locationMapper.toEntity(locationDTO);
        location = locationRepository.save(location);
        LocationDTO result = locationMapper.toDto(location);
        locationSearchRepository.save(location);
        return result;
    }

    @Override
    public Optional<LocationDTO> partialUpdate(LocationDTO locationDTO) {
        log.debug("Request to partially update Location : {}", locationDTO);

        return locationRepository
            .findById(locationDTO.getId())
            .map(
                existingLocation -> {
                    locationMapper.partialUpdate(existingLocation, locationDTO);

                    return existingLocation;
                }
            )
            .map(locationRepository::save)
            .map(
                savedLocation -> {
                    locationSearchRepository.save(savedLocation);

                    return savedLocation;
                }
            )
            .map(locationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDTO> findAll() {
        log.debug("Request to get all Locations");
        return locationRepository.findAll().stream().map(locationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationDTO> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id).map(locationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
        locationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDTO> search(String query) {
        log.debug("Request to search Locations for query {}", query);
        return StreamSupport
            .stream(locationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(locationMapper::toDto)
            .collect(Collectors.toList());
    }
}
