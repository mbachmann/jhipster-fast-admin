package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.EffortDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Effort} and its DTO {@link EffortDTO}.
 */
@Mapper(componentModel = "spring", uses = { ReportingActivityMapper.class })
public interface EffortMapper extends EntityMapper<EffortDTO, Effort> {
    @Mapping(target = "activity", source = "activity", qualifiedByName = "id")
    EffortDTO toDto(Effort s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EffortDTO toDtoId(Effort effort);
}
