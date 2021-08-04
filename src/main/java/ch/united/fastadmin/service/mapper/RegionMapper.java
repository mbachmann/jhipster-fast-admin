package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.RegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Region} and its DTO {@link RegionDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class })
public interface RegionMapper extends EntityMapper<RegionDTO, Region> {
    @Mapping(target = "country", source = "country", qualifiedByName = "id")
    RegionDTO toDto(Region s);
}
