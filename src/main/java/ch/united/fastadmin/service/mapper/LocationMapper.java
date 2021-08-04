package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepartmentMapper.class })
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "department", source = "department", qualifiedByName = "id")
    LocationDTO toDto(Location s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoId(Location location);
}
