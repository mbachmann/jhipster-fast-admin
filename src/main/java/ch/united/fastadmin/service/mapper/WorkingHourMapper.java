package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.WorkingHourDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkingHour} and its DTO {@link WorkingHourDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicationUserMapper.class, EffortMapper.class })
public interface WorkingHourMapper extends EntityMapper<WorkingHourDTO, WorkingHour> {
    @Mapping(target = "applicationUser", source = "applicationUser", qualifiedByName = "id")
    @Mapping(target = "effort", source = "effort", qualifiedByName = "id")
    WorkingHourDTO toDto(WorkingHour s);
}
