package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ReportingActivityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportingActivity} and its DTO {@link ReportingActivityDTO}.
 */
@Mapper(componentModel = "spring", uses = { CatalogServiceMapper.class })
public interface ReportingActivityMapper extends EntityMapper<ReportingActivityDTO, ReportingActivity> {
    @Mapping(target = "catalogService", source = "catalogService", qualifiedByName = "id")
    ReportingActivityDTO toDto(ReportingActivity s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportingActivityDTO toDtoId(ReportingActivity reportingActivity);
}
